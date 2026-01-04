/*
 * Copyright 2025-present Samuel Castrillo
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.github.anvil.processor;

import io.github.anvil.Schema;
import io.github.anvil.annotations.OptionalValue;
import io.github.anvil.annotations.Validate;
import io.github.anvil.exceptions.CannotSetValueException;
import io.github.anvil.exceptions.NonConstructibleException;
import io.github.anvil.exceptions.ValidationException;
import io.github.anvil.restriction.RestrictionChecker;
import io.github.anvil.validation.ValidationError;
import io.github.anvil.validation.ValidationErrors;
import io.github.anvil.validation.Validator;
import io.github.anvil.validation.ValidatorRegistry;
import io.github.anvil.validation.validators.InnerValidator;
import org.slf4j.Logger;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.RecordComponent;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Base processor that converts an input representation into a validated {@link Schema} instance.
 *
 * <p>Subclasses are responsible for providing concrete ways to extract field values from the
 * input type {@code IN} and a logger implementation.</p>
 *
 * @param <IN> the input type from which field values are read.
 */
public abstract class Processor<IN> {
    private final RestrictionChecker restrictionChecker = new RestrictionChecker();
    private final ValidatorRegistry validatorRegistry = ValidatorRegistry.getInstance(); // Made a field to avoid repeated calls

    protected Processor() {
        this.validatorRegistry.addNonOverridingValidator(new InnerValidator(this));
    }

    /**
     * Extracts a boolean field value from the input.
     *
     * @param input     the input source.
     * @param fieldName the name of the field to read.
     * @return the boolean value for the given field, or {@code null} if not present.
     */
    public abstract Boolean getBooleanFieldValue(IN input, String fieldName);

    /**
     * Extracts a numeric field value of the given type from the input.
     *
     * @param input       the input source.
     * @param numberClass the numeric type expected for the field.
     * @param fieldName   the name of the field to read.
     * @return the numeric value for the given field, or {@code null} if not present.
     */
    public abstract Number getNumberFieldValue(IN input, Class<?> numberClass, String fieldName);

    /**
     * Extracts a string field value from the input.
     *
     * @param input     the input source.
     * @param fieldName the name of the field to read.
     * @return the string value for the given field, or {@code null} if not present.
     */
    public abstract String getStringFieldValue(IN input, String fieldName);

    /**
     * Extracts a nested input representation for a field marked as an inner schema.
     *
     * @param input     the input source.
     * @param fieldName the name of the field to read.
     * @return the nested input representation for the given field, or {@code null} if not present.
     */
    public abstract IN getInnerInput(IN input, String fieldName);

    /**
     * Returns the logger to be used by this processor.
     *
     * @return the logger instance.
     */
    public abstract Logger getLogger();

    /**
     * Processes the input into a validated schema instance of the given class.
     *
     * <p>The method validates all fields according to their annotations, optionally failing fast,
     * and finally constructs and populates the target schema instance when there are no errors.</p>
     *
     * @param input the input to read values from.
     * @param clazz the schema class to instantiate.
     * @param <OUT> the schema subtype to be created.
     * @return the populated and validated schema instance, or {@code null} if there were validation errors.
     * @throws ValidationException if fail-fast mode is enabled and a validation error occurs.
     */
    public final <OUT extends Schema> OUT process(IN input, Class<OUT> clazz) throws ValidationException {
        this.checkSchema(clazz);

        boolean failFast = this.isFailFast(clazz);
        ValidationErrors validationErrors = new ValidationErrors(failFast);
        Map<Field, Object> fieldsToAssign = new HashMap<>();

        for (Field field : clazz.getDeclaredFields()) {
            String fieldName = field.getName();
            Object inputValue = this.inputValue(input, fieldName, field.getType());
            Object valueToAssign = inputValue;

            for (Annotation annotation : field.getAnnotations()) {
                Validator validator = this.validatorRegistry.getValidator(annotation.annotationType());

                try {
                    Object validated = validator.validate(inputValue, fieldName, annotation);

                    if (validated != null) {
                        valueToAssign = validated; // Keep the last non-null validated input value
                    }
                } catch (ValidationError error) {
                    // Special handling for optional fields:
                    //  - if the field is optional and the input value is null, skip adding the error
                    //  - otherwise, add the error as usual
                    // This scenario can occur when the field is not provided in the input.
                    // If it is provided, the checks will be applied as usual and errors added.
                    if (isOptionalField(field) && inputValue == null) {
                        valueToAssign = null;
                    } else {
                        validationErrors.addError(error);
                    }
                } catch (ValidationException e) {
                    // Handle ValidationException from validators (e.g., InnerValidator with nested errors)
                    // Extract all errors and add them to the collection
                    // Special handling for optional fields: if the field is optional and the input value is null,
                    // skip adding the errors (similar to ValidationError handling above)
                    if (!isOptionalField(field) || inputValue != null) {
                        for (ValidationError error : e.getErrors()) {
                            validationErrors.addError(error);
                        }
                    }

                    // Don't assign the raw input value if validation failed - set to null to prevent
                    // passing raw input (e.g., JsonObject) to record/class constructors
                    valueToAssign = null;
                }
            }

            fieldsToAssign.put(field, valueToAssign);
        }

        OUT constructedObject = this.constructObject(clazz, fieldsToAssign);

        try {
            constructedObject.postBuild();
        } catch (ValidationError error) {
            validationErrors.addError(error);
        }

        validationErrors.throwIfAny();

        return constructedObject;
    }

    /**
     * Processes the input into a validated schema instance of the given class.
     *
     * <p>This is a type-safe wrapper around {@link #process(Object, Class)}.</p>
     *
     * @param value    the input to read values from.
     * @param outClass the schema class to instantiate.
     * @return the populated and validated schema instance, or {@code null} if there were validation errors.
     * @throws ValidationException if fail-fast mode is enabled and a validation error occurs.
     */
    @SuppressWarnings("unchecked")
    public final Schema processUnchecked(Object value, Class<? extends Schema> outClass) throws ValidationException {
        return this.process((IN) value, outClass);
    }

    /**
     * Constructs an instance of the given schema class and assigns the provided field values.
     *
     * @param <OUT>          the schema subtype.
     * @param clazz          the schema class to instantiate.
     * @param fieldsToAssign the field values to assign to the instance.
     * @return the constructed schema instance.
     */
    private <OUT extends Schema> OUT constructObject(Class<OUT> clazz, Map<Field, Object> fieldsToAssign) {
        if (clazz.isRecord()) {
            return createRecordInstance(clazz, fieldsToAssign);
        }

        return createClassInstance(clazz, fieldsToAssign);
    }

    /**
     * Determines whether a field is optional according to its {@link OptionalValue} annotation presence.
     *
     * @param field the field to inspect.
     * @return {@code true} if the field is marked as optional, {@code false} otherwise.
     */
    private static boolean isOptionalField(Field field) {
        return field.getAnnotation(OptionalValue.class) != null;
    }

    /**
     * Reads a single character value from the input for the given field name.
     *
     * @param input     the input source.
     * @param fieldName the name of the field to read.
     * @return the character value.
     * @throws IllegalArgumentException if the underlying string is not exactly one character long.
     */
    private Character getCharacterFieldValue(IN input, String fieldName) {
        return Optional.ofNullable(getStringFieldValue(input, fieldName))
                       .filter(s -> s.length() == 1)
                       .map(s -> s.charAt(0))
                       .orElseThrow(
                           () -> new IllegalArgumentException("Expected a single character for field: " + fieldName));
    }

    /**
     * Resolves the raw input value for a field based on its declared type.
     *
     * @param input     the input source.
     * @param fieldName the name of the field to read.
     * @param fieldType the declared type of the field.
     * @return the resolved value, or {@code null} if the field is not present in the input.
     */
    private Object inputValue(IN input, String fieldName, Class<?> fieldType) {
        try {
            if (fieldType == Boolean.class || fieldType == boolean.class) {
                return getBooleanFieldValue(input, fieldName);
            }

            if (fieldType == Character.class || fieldType == char.class) {
                return getCharacterFieldValue(input, fieldName);
            }

            if (isNumericType(fieldType)) {
                return getNumberFieldValue(input, fieldType, fieldName);
            }

            if (fieldType == String.class || fieldType.isEnum()) {
                return getStringFieldValue(input, fieldName);
            }

            return getInnerInput(input, fieldName);
        } catch (NullPointerException e) {
            return null; // Field not present in input
        }
    }

    /**
     * Creates a new instance of the given schema class, using either a no-argument constructor
     * plus field assignment or, if unavailable, an all-arguments constructor.
     *
     * @param <OUT>          the schema subtype.
     * @param clazz          the schema class to instantiate.
     * @param fieldsToAssign the validated values for each declared field of the schema.
     * @return a new instance of the given class populated with the provided field values.
     * @throws NonConstructibleException if no suitable constructor is found or instantiation fails.
     */
    private <OUT extends Schema> OUT createClassInstance(Class<OUT> clazz, Map<Field, Object> fieldsToAssign) {
        try {
            OUT instance;
            Constructor<?>[] declaredConstructors = clazz.getDeclaredConstructors();
            Optional<Constructor<?>> noArgsConstructor = Arrays.stream(declaredConstructors)
                                                               .filter(c -> c.getParameterCount() == 0)
                                                               .findFirst();

            if (noArgsConstructor.isPresent()) {
                instance = clazz.getDeclaredConstructor().newInstance();
                fieldsToAssign.forEach((field, value) -> setFieldValue(instance, field, value));
            } else {
                Field[] fields = clazz.getDeclaredFields();
                Constructor<?> allArgsConstructor = Arrays.stream(declaredConstructors)
                                                          .filter(c -> c.getParameterCount() == fields.length)
                                                          .findFirst()
                                                          .orElseThrow(() -> new NoSuchMethodException(
                                                              "No suitable constructor found for " + clazz.getName()));

                instance = clazz.getDeclaredConstructor(allArgsConstructor.getParameterTypes())
                                .newInstance(Arrays.stream(fields).map(fieldsToAssign::get).toArray());
            }

            return instance;
        } catch (ReflectiveOperationException e) {
            throw new NonConstructibleException(clazz, e);
        }
    }

    /**
     * Creates a new instance of the given record-based schema class using its canonical constructor.
     *
     * <p>Constructor arguments are resolved by matching record component names to the keys in
     * {@code fieldsToAssign}. Any component without a matching entry receives {@code null}.</p>
     *
     * @param <OUT>          the schema subtype.
     * @param clazz          the record schema class to instantiate.
     * @param fieldsToAssign the validated values for each declared field of the schema.
     * @return a new record instance populated with the provided field values.
     * @throws NonConstructibleException if the record cannot be instantiated.
     */
    private <OUT extends Schema> OUT createRecordInstance(Class<OUT> clazz, Map<Field, Object> fieldsToAssign) {
        try {
            RecordComponent[] recordComponents = clazz.getRecordComponents();
            Class<?>[] parameterTypes = Arrays.stream(recordComponents)
                                              .map(RecordComponent::getType)
                                              .toArray(Class[]::new);

            Object[] constructorArgs =
                Arrays.stream(recordComponents)
                      .map(rc -> fieldsToAssign.entrySet()
                                               .stream()
                                               .filter(e -> e.getKey().getName().equals(rc.getName()))
                                               .findFirst()
                                               .map(Map.Entry::getValue)
                                               .orElse(null))
                      .toArray();

            return clazz.getDeclaredConstructor(parameterTypes).newInstance(constructorArgs);
        } catch (ReflectiveOperationException e) {
            throw new NonConstructibleException(clazz, e);
        }
    }

    /**
     * Sets the given value on the specified field of the schema instance.
     *
     * @param instance the schema instance whose field should be set.
     * @param field    the field to modify.
     * @param value    the value to assign to the field.
     * @param <OUT>    the schema subtype.
     * @throws CannotSetValueException if the field cannot be updated.
     */
    private <OUT extends Schema> void setFieldValue(OUT instance, Field field, Object value) {
        try {
            boolean oldAccessible = field.canAccess(instance);
            field.setAccessible(true);
            field.set(instance, value);
            field.setAccessible(oldAccessible);
        } catch (ReflectiveOperationException e) {
            throw new CannotSetValueException(field, value, e);
        }
    }

    /**
     * Determines whether the given schema class is configured for fail-fast validation.
     *
     * @param clazz the schema class to inspect.
     * @return {@code true} if fail-fast is enabled, {@code false} otherwise.
     */
    private boolean isFailFast(Class<?> clazz) {
        Validate validateAnnotation = clazz.getAnnotation(Validate.class);
        return validateAnnotation != null && validateAnnotation.failFast();
    }

    /**
     * Ensures that the given class is a valid schema configuration and optionally prints
     * diagnostic information.
     *
     * @param clazz the schema class to check.
     */
    private void checkSchema(Class<?> clazz) {
        Validate validateAnnotation = clazz.getAnnotation(Validate.class);

        if (validateAnnotation == null) {
            throw new IllegalStateException("Class " + clazz.getName() + " is not annotated with @Validate");
        }

        if (!validateAnnotation.value()) {
            throw new IllegalStateException("Validation is disabled for class " + clazz.getName());
        }

        Field[] declaredFields = clazz.getDeclaredFields();
        Arrays.stream(declaredFields).forEach(this.restrictionChecker::checkAnnotationRestrictions);

        if (validateAnnotation.printInfo()) {
            getLogger().info("Class {} is marked for validation.", clazz.getName());
            printClassInfo(declaredFields);
        }
    }

    /**
     * Logs information about the declared fields and their annotations.
     *
     * @param fields the fields whose information should be logged.
     */
    private void printClassInfo(Field[] fields) {
        Logger logger = getLogger();

        Arrays.stream(fields)
              .forEachOrdered(field -> {
                  logger.info("Field: {}", field.getName());
                  for (var annotation : field.getAnnotations()) {
                      logger.info("  {}", annotation.annotationType().getName());
                  }
              });
    }

    /**
     * Determines whether the given type is numeric (a {@link Number} or a primitive
     * numeric type other than {@code boolean} and {@code char}).
     *
     * @param type the type to inspect.
     * @return {@code true} if the type is numeric, {@code false} otherwise.
     */
    private static boolean isNumericType(Class<?> type) {
        return Number.class.isAssignableFrom(type)
            || (type.isPrimitive() && type != boolean.class && type != char.class);
    }
}
