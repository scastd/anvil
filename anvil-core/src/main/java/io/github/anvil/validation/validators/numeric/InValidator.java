package io.github.anvil.validation.validators.numeric;

import io.github.anvil.annotations.numeric.In;
import io.github.anvil.validation.ValidationError;

import java.lang.annotation.Annotation;
import java.util.Arrays;

/**
 * Validator for the {@link In} annotation, enforcing membership in a set of valid values.
 */
public class InValidator extends NumericValidator {

    /**
     * Validates that the given numeric value is contained in the configured list of valid values.
     *
     * @param value      the value to validate.
     * @param fieldName  the name of the field being validated.
     * @param annotation the {@link In} annotation instance.
     * @return {@code null} if validation succeeds.
     * @throws ValidationError if the configured list is empty or the value is not contained in it.
     */
    @Override
    public Object validate(Object value, String fieldName, Annotation annotation) throws ValidationError {
        In in = (In) annotation;
        double[] validValues = in.value();
        double number = this.getNumber(value, fieldName).doubleValue();

        if (validValues.length == 0) {
            throw new ValidationError(
                "At least one valid value must be specified for the '@In' validation on field '%s'.".formatted(
                    fieldName));
        }

        for (double validValue : validValues) {
            if (number == validValue) {
                return null;
            }
        }

        throw new ValidationError(
            "Found value '%s' for field '%s', but expected one of: %s.".formatted(number, fieldName,
                                                                                  Arrays.toString(validValues)));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Class<? extends Annotation> getSupportedAnnotation() {
        return In.class;
    }
}
