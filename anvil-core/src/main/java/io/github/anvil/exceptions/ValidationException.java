/*
 * Copyright 2025-present Samuel Castrillo
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
            throw new IllegalArgumentException("ValidationException requires at least one validation error.");
        }

        String collect = validationErrors.stream()
                                         .map(ValidationError::getMessage)
                                         .collect(Collectors.joining("\n\t- "));

        return "Validation failed with %d error(s):\n\t- %s".formatted(validationErrors.size(), collect);
    }
}
