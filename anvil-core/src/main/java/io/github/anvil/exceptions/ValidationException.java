package io.github.anvil.exceptions;

import io.github.anvil.validation.ValidationError;

import java.util.Collection;
import java.util.stream.Collectors;

/**
 * Aggregates one or more {@link ValidationError} instances into a single runtime exception.
 */
public class ValidationException extends RuntimeException {

    /**
     * Creates a new exception from a collection of validation errors.
     *
     * @param validationErrors the collection of validation errors that occurred.
     */
    public ValidationException(Collection<ValidationError> validationErrors) {
        super(formatErrors(validationErrors));
    }

    /**
     * Formats a collection of validation errors into a human-readable message.
     *
     * @param validationErrors the collection of validation errors to format.
     * @return a multi-line message summarizing all validation errors.
     */
    private static String formatErrors(Collection<ValidationError> validationErrors) {
        String collect = validationErrors.stream()
                                         .map(ValidationError::getMessage)
                                         .collect(Collectors.joining("\n\t- "));

        return "Validation failed with %d error(s):\n\t- %s".formatted(validationErrors.size(), collect);
    }
}
