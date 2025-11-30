package io.github.anvil.exceptions;

import java.lang.reflect.Field;

/**
 * Thrown when a value cannot be assigned to a field during schema population.
 */
public class CannotSetValueException extends RuntimeException {

    /**
     * Creates a new exception indicating that the given value could not be set on the specified field.
     *
     * @param field the field that failed to be set.
     * @param value the value that was attempted to be assigned.
     * @param cause the underlying cause of the failure.
     */
    public CannotSetValueException(Field field, Object value, Throwable cause) {
        super(
            "Cannot set value '%s' to field '%s' of type '%s'.".formatted(
                value, field.getName(), field.getType().getName()
            ),
            cause
        );
    }
}
