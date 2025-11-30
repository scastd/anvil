package io.github.anvil.validation.validators.numeric;

import io.github.anvil.annotations.numeric.Greater;
import io.github.anvil.validation.ValidationError;

import java.lang.annotation.Annotation;

/**
 * Validator for the {@link Greater} annotation, enforcing a strictly greater-than constraint.
 */
public class GreaterValidator extends NumericValidator {

    /**
     * Validates that the given numeric value is strictly greater than the configured {@link Greater#value()}.
     *
     * @param value      the value to validate.
     * @param fieldName  the name of the field being validated.
     * @param annotation the {@link Greater} annotation instance.
     * @return {@code null} if validation succeeds.
     * @throws ValidationError if the value is less than or equal to the threshold.
     */
    @Override
    public Object validate(Object value, String fieldName, Annotation annotation) throws ValidationError {
        Greater greater = (Greater) annotation;
        double number = this.getNumber(value, fieldName).doubleValue();

        if (number <= greater.value()) {
            throw new ValidationError(
                "Field '%s' must be greater than the specified value (%s).".formatted(fieldName, greater.value()));
        }

        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Class<? extends Annotation> getSupportedAnnotation() {
        return Greater.class;
    }
}
