package io.github.anvil.validation;

/**
 * Represents a single validation failure for a specific field or value.
 */
public final class ValidationError extends Exception {

    /**
     * Creates a new validation error with the given message.
     *
     * @param message the detail message describing the validation failure.
     * @throws IllegalArgumentException if the message is null or blank.
     */
    public ValidationError(String message) {
        super(requireNonBlank(message));
    }

    /**
     * Validates that the message is not null or blank.
     *
     * @param message the message to validate.
     * @return the message if it is valid.
     * @throws IllegalArgumentException if the message is null or blank.
     */
    private static String requireNonBlank(String message) {
        if (message == null) {
            throw new IllegalArgumentException("Validation error message cannot be null");
        }

        if (message.isBlank()) {
            throw new IllegalArgumentException("Validation error message cannot be blank");
        }

        return message;
    }
}
