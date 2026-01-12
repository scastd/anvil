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

package io.github.anvil.validation.validators;

import io.github.anvil.Schema;
import io.github.anvil.annotations.List;
import io.github.anvil.exceptions.ValidationException;
import io.github.anvil.processor.Processor;
import io.github.anvil.validation.ValidationError;
import io.github.anvil.validation.Validator;

import java.lang.annotation.Annotation;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Validator implementation for the {@link List} annotation.
 *
 * <p>This validator handles list validation by processing each element in the list as a nested
 * schema instance. When a field is annotated with {@code @List(SchemaClass.class)}, this validator
 * extracts the schema class from the annotation and uses the processor to validate and construct
 * each element in the list.</p>
 *
 * <p>The validator is registered dynamically by the processor when it encounters fields annotated
 * with {@link List}. This allows list elements to be validated recursively, ensuring that all
 * validation rules defined in the element schema class are applied to each element.</p>
 *
 * <p>If validation of any list element fails, a {@link ValidationException} is thrown, which
 * will be handled according to the parent schema's fail-fast configuration. Error messages are
 * prefixed with the field name and element index (e.g., "items[0].name") to provide clear context
 * about which element and field has the error.</p>
 *
 * @see List
 * @see Validator
 * @see Processor
 */
public class ListValidator implements Validator {
    private final Processor<?> processor;

    /**
     * Creates a new instance with the specified processor.
     *
     * @param processor the processor to use for validating list elements.
     */
    public ListValidator(Processor<?> processor) {
        this.processor = processor;
    }

    /**
     * Validates each element in the given list as a nested schema instance.
     *
     * <p>This method extracts the schema class from the {@link List} annotation and uses the
     * processor to validate and construct each element in the list. The element validation follows
     * the same rules as top-level schema validation.</p>
     *
     * <p>When element validation fails, all errors are extracted and prefixed with the field path
     * and element index (e.g., "items[0].street" instead of just "street") to provide clear context
     * about which list element and field has the error. All nested errors are preserved and thrown
     * as a {@link ValidationException} (unchecked) so they can all be collected by the processor.</p>
     *
     * @param value      the list input value to validate (must be a Collection or array).
     * @param fieldName  the name of the field being validated (used for error messages).
     * @param annotation the {@link List} annotation instance.
     * @return a list containing the validated and constructed schema instances.
     * @throws ValidationError     if the value is null or not a collection/array, or if validation
     *                             of a list element fails (for single errors).
     * @throws ValidationException if validation of list elements fails with multiple errors.
     *                             The exception contains all nested errors with field paths prefixed
     *                             with the list field name and element index.
     */
    @Override
    public java.util.List<Schema> validate(Object value, String fieldName, Annotation annotation) throws ValidationError {
        List listAnnotation = (List) annotation;
        Class<? extends Schema> schemaClass = listAnnotation.value();

        if (value == null) {
            throw new ValidationError("for field '%s': List value cannot be null.".formatted(fieldName));
        }

        Collection<?> collection = convertToCollection(value);
        java.util.List<Schema> validatedElements = new ArrayList<>();
        java.util.List<ValidationError> allErrors = new ArrayList<>();

        int index = 0;
        for (Object element : collection) {
            final int currentIndex = index;
            try {
                Schema validatedElement = this.processor.processUnchecked(element, schemaClass);
                validatedElements.add(validatedElement);
            } catch (ValidationException e) {
                // Extract all nested errors and prefix them with the field path and index
                // Format: "items[0].fieldName: error message" for better readability
                java.util.List<ValidationError> prefixedErrors = e.getErrors()
                                                                  .stream()
                                                                  .map(error -> new ValidationError(
                                                                      prefixErrorMessage(error.getMessage(), fieldName,
                                                                                         currentIndex)))
                                                                  .toList();
                allErrors.addAll(prefixedErrors);
            }

            index++;
        }

        if (!allErrors.isEmpty()) {
            throw new ValidationException(allErrors);
        }

        return validatedElements;
    }

    /**
     * Converts the given value to a Collection, handling both Collection instances and arrays.
     *
     * @param value the value to convert.
     * @return a Collection containing the elements.
     * @throws ValidationError if the value is not a Collection or array.
     */
    private Collection<?> convertToCollection(Object value) throws ValidationError {
        if (value instanceof Collection) {
            return (Collection<?>) value;
        }

        if (value.getClass().isArray()) {
            int length = Array.getLength(value);
            java.util.List<Object> list = new ArrayList<>(length);

            for (int i = 0; i < length; i++) {
                list.add(Array.get(value, i));
            }

            return list;
        }

        throw new ValidationError(
            "Value must be a Collection or array, but was: %s.".formatted(value.getClass().getName()));
    }

    /**
     * Prefixes an error message with the parent field name and element index to build the full path from the root.
     *
     * <p>This method replaces "for field 'fieldName'" with "for field 'parentField[index].fieldName'"
     * to show the complete path. It handles both simple field names and already-prefixed paths
     * (for deeply nested structures).</p>
     *
     * @param errorMessage    the original error message.
     * @param parentFieldName the parent field name to prefix.
     * @param index           the index of the element in the list.
     * @return the error message with the parent field name and index prefixed.
     */
    private String prefixErrorMessage(String errorMessage, String parentFieldName, int index) {
        String indexedFieldName = "%s[%d]".formatted(parentFieldName, index);
        String prefixedMessage = errorMessage.replaceFirst(
            "for field '([^']+)'",
            "for field '%s.$1'".formatted(indexedFieldName)
        );
        // If the replacement didn't match (different error message format),
        // prefix the entire message with the parent field name and index
        if (prefixedMessage.equals(errorMessage)) {
            prefixedMessage = "%s: %s".formatted(indexedFieldName, errorMessage);
        }
        return prefixedMessage;
    }

    /**
     * Returns the annotation type supported by this validator.
     *
     * @return {@link List} class.
     */
    @Override
    public Class<? extends Annotation> getSupportedAnnotation() {
        return List.class;
    }
}
