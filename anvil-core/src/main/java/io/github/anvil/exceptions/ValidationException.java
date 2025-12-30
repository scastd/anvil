package io.github.anvil.exceptions;

import io.github.anvil.validation.ValidationError;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Aggregates one or more {@link ValidationError} instances into a single runtime exception.
 */
public class ValidationException extends RuntimeException {
    private final List<ValidationError> errors;

    /**
     * Creates a new exception from a collection of validation errors.
     *
     * @param validationErrors the collection of validation errors that occurred.
     * @throws IllegalArgumentException if the collection is empty or null.
     */
    public ValidationException(Collection<ValidationError> validationErrors) {
        super(formatErrors(validationErrors));
        this.errors = new ArrayList<>(validationErrors);
    }

    /**
     * Returns the list of validation errors that caused this exception.
     *
     * @return an unmodifiable list of validation errors.
     */
    public List<ValidationError> getErrors() {
        return Collections.unmodifiableList(errors);
    }

    /**
     * Formats a collection of validation errors into a human-readable message.
     *
     * @param validationErrors the collection of validation errors to format.
     * @return a multi-line message summarizing all validation errors.
     * @throws IllegalArgumentException if the collection is null or empty.
     */
    private static String formatErrors(Collection<ValidationError> validationErrors) {
        if (validationErrors == null || validationErrors.isEmpty()) {
            throw new IllegalArgumentException("ValidationException requires at least one validation error");
        }

        String collect = validationErrors.stream()
                                         .map(ValidationError::getMessage)
                                         .collect(Collectors.joining("\n\t- "));

        return "Validation failed with %d error(s):\n\t- %s".formatted(validationErrors.size(), collect);
    }
}
