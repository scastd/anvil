package io.github.anvil.validation.validators.numeric;

import io.github.anvil.annotations.numeric.Less;
import io.github.anvil.validation.ValidationError;

import java.lang.annotation.Annotation;

/**
 * Validator for the {@link Less} annotation, enforcing a strictly less-than constraint.
 */
public class LessValidator extends NumericValidator {

    /**
     * Validates that the given numeric value is strictly less than the configured {@link Less#value()}.
     *
     * @param value      the value to validate.
     * @param fieldName  the name of the field being validated.
     * @param annotation the {@link Less} annotation instance.
     * @return {@code null} if validation succeeds.
     * @throws ValidationError if the value is greater than or equal to the threshold.
     */
    @Override
    public Object validate(Object value, String fieldName, Annotation annotation) throws ValidationError {
        Less less = (Less) annotation;
        double number = this.getNumber(value, fieldName).doubleValue();

        if (number >= less.value()) {
            throw new ValidationError(
                "Field '%s' must be less than the specified value (%s).".formatted(fieldName, less.value()));
        }

        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Class<? extends Annotation> getSupportedAnnotation() {
        return Less.class;
    }
}
