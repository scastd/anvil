package io.github.anvil.validation;

/**
 * Represents a single validation failure for a specific field or value.
 */
public final class ValidationError extends Exception {

    /**
     * Creates a new validation error with the given message.
     *
     * @param message the detail message describing the validation failure.
     */
    public ValidationError(String message) {
        super(message);
    }
}
