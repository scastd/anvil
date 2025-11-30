package io.github.anvil.validation.validators.numeric;

import io.github.anvil.annotations.numeric.GreaterOrEqual;
import io.github.anvil.validation.ValidationError;

import java.lang.annotation.Annotation;

/**
 * Validator for the {@link GreaterOrEqual} annotation, enforcing a greater-than-or-equal-to constraint.
 */
public class GreaterOrEqualValidator extends NumericValidator {

    /**
     * Validates that the given numeric value is greater than or equal to the configured {@link GreaterOrEqual#value()}.
     *
     * @param value      the value to validate.
     * @param fieldName  the name of the field being validated.
     * @param annotation the {@link GreaterOrEqual} annotation instance.
     * @return {@code null} if validation succeeds.
     * @throws ValidationError if the value is less than the threshold.
     */
    @Override
    public Object validate(Object value, String fieldName, Annotation annotation) throws ValidationError {
        GreaterOrEqual greaterOrEqual = (GreaterOrEqual) annotation;
        double number = this.getNumber(value, fieldName).doubleValue();

        if (number < greaterOrEqual.value()) {
            throw new ValidationError(
                "Field '%s' must be greater than or equal to the specified value (%s).".formatted(fieldName,
                                                                                                  greaterOrEqual.value()));
        }

        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Class<? extends Annotation> getSupportedAnnotation() {
        return GreaterOrEqual.class;
    }
}
