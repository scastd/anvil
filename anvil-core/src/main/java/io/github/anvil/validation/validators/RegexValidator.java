package io.github.anvil.validation.validators;

import io.github.anvil.annotations.Regex;
import io.github.anvil.validation.ValidationError;
import io.github.anvil.validation.Validator;

import java.lang.annotation.Annotation;

/**
 * Validator for the {@link Regex} annotation, enforcing a regular expression on string values.
 */
public class RegexValidator implements Validator {

    /**
     * Validates that the given string value matches the {@link Regex#value()} pattern.
     *
     * @param value      the value to validate.
     * @param fieldName  the name of the field being validated.
     * @param annotation the {@link Regex} annotation instance.
     * @return {@code null} if validation succeeds.
     * @throws ValidationError if the value is {@code null} or does not match the pattern.
     */
    @Override
    public Object validate(Object value, String fieldName, Annotation annotation) throws ValidationError {
        Regex regex = (Regex) annotation;
        String pattern = regex.value();

        if (value == null || !((String) value).matches(pattern)) {
            throw new ValidationError(
                "Value '%s' for field '%s' does not match the required pattern: '%s'.".formatted(value, fieldName,
                                                                                                 pattern));
        }

        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Class<? extends Annotation> getSupportedAnnotation() {
        return Regex.class;
    }
}
