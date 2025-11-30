package io.github.anvil.validation.validators;

import io.github.anvil.annotations.StrEqual;
import io.github.anvil.validation.ValidationError;
import io.github.anvil.validation.Validator;

import java.lang.annotation.Annotation;

/**
 * Validator for the {@link StrEqual} annotation, enforcing string equality with a configurable strategy.
 */
public class StrEqualValidator implements Validator {

    /**
     * Validates that the given string value equals the expected {@link StrEqual#value()} using
     * the configured {@link StrEqual#strategy()}.
     *
     * @param value      the value to validate.
     * @param fieldName  the name of the field being validated.
     * @param annotation the {@link StrEqual} annotation instance.
     * @return {@code null} if validation succeeds.
     * @throws ValidationError if the value does not equal the expected string.
     */
    @Override
    public Object validate(Object value, String fieldName, Annotation annotation) throws ValidationError {
        StrEqual strEqual = (StrEqual) annotation;
        String expectedValue = strEqual.value();

        if (!StringComparer.equal((String) value, expectedValue, strEqual.strategy())) {
            throw new ValidationError(
                "Found value '%s' for field '%s', but expected equal to: '%s'.".formatted(value, fieldName,
                                                                                          expectedValue));
        }

        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Class<? extends Annotation> getSupportedAnnotation() {
        return StrEqual.class;
    }
}
