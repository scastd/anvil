package io.github.anvil.validation.validators.numeric;

import io.github.anvil.validation.ValidationError;
import io.github.anvil.validation.Validator;

/**
 * Base validator for numeric constraints, providing common number extraction logic.
 */
public abstract class NumericValidator implements Validator {

    /**
     * Ensures that the given value is a {@link Number} and returns it.
     *
     * @param value     the value to validate and cast.
     * @param fieldName the name of the field being validated.
     * @return the value as a {@link Number}.
     * @throws ValidationError if the value is not numeric.
     */
    protected Number getNumber(Object value, String fieldName) throws ValidationError {
        if (value instanceof Number number) {
            return number;
        }

        throw new ValidationError("Field '%s' is not a number.".formatted(fieldName));
    }
}
