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
            throw new IllegalArgumentException("Validation error message cannot be null.");
        }

        if (message.isBlank()) {
            throw new IllegalArgumentException("Validation error message cannot be blank.");
        }

        return message;
    }
}
