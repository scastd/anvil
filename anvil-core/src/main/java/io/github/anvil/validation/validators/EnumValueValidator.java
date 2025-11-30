package io.github.anvil.validation.validators;

import io.github.anvil.annotations.EnumValue;
import io.github.anvil.validation.ValidationError;
import io.github.anvil.validation.Validator;

import java.lang.annotation.Annotation;
import java.util.List;
import java.util.stream.Stream;

/**
 * Validator for the {@link EnumValue} annotation, ensuring a string matches one of an enum's constants.
 */
public class EnumValueValidator implements Validator {

    /**
     * Validates that the given string value corresponds to one of the enum constants declared by
     * the {@link EnumValue#value()} enum type.
     *
     * @param value      the value to validate.
     * @param fieldName  the name of the field being validated.
     * @param annotation the {@link EnumValue} annotation instance.
     * @return the matching enum constant if validation succeeds.
     * @throws ValidationError if the value is not a string or does not match any enum constant.
     */
    @Override
    public Object validate(Object value, String fieldName, Annotation annotation) throws ValidationError {
        if (!(value instanceof String stringValue)) {
            throw new ValidationError(
                "Field '%s' is not a string (%s), cannot validate enum value.".formatted(fieldName, value));
        }

        EnumValue enumValue = (EnumValue) annotation;
        Class<? extends Enum<?>> enumClass = enumValue.value();
        Enum<?>[] enumConstants = enumClass.getEnumConstants();
        List<String> allowedValues = Stream.of(enumConstants).map(Enum::name).toList();

        return Stream.of(enumConstants)
                     .filter(enumConstant -> enumConstant.name().equals(value))
                     .findFirst()
                     .orElseThrow(() -> new ValidationError(
                         "Field '%s' has value '%s' which is not among the allowed enum values for the enum '%s': %s".formatted(
                             fieldName, stringValue, enumClass.getSimpleName(), allowedValues)));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Class<? extends Annotation> getSupportedAnnotation() {
        return EnumValue.class;
    }
}
