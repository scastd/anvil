package io.github.anvil.validation.validators.numeric;

import io.github.anvil.annotations.numeric.Equal;
import io.github.anvil.validation.ValidationError;

import java.lang.annotation.Annotation;

/**
 * Validator for the {@link Equal} annotation, enforcing exact numeric equality.
 */
public class EqualValidator extends NumericValidator {

    /**
     * Validates that the given numeric value is exactly equal to the configured {@link Equal#value()}.
     *
     * @param value      the value to validate.
     * @param fieldName  the name of the field being validated.
     * @param annotation the {@link Equal} annotation instance.
     * @return {@code null} if validation succeeds.
     * @throws ValidationError if the value is not equal to the expected value.
     */
    @Override
    public Object validate(Object value, String fieldName, Annotation annotation) throws ValidationError {
        Equal equal = (Equal) annotation;
        double number = this.getNumber(value, fieldName).doubleValue();

        if (number != equal.value()) {
            throw new ValidationError(
                "Field '%s' must be equal to the specified value (%s).".formatted(fieldName, equal.value()));
        }

        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Class<? extends Annotation> getSupportedAnnotation() {
        return Equal.class;
    }
}
