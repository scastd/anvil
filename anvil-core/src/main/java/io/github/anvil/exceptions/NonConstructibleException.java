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

/**
 * Thrown when an instance of a schema class cannot be created.
 */
public class NonConstructibleException extends RuntimeException {

    /**
     * Creates a new exception indicating that the given class could not be instantiated.
     *
     * @param clazz the class that failed to instantiate.
     * @param cause the underlying cause of the instantiation failure.
     */
    public NonConstructibleException(Class<?> clazz, Throwable cause) {
        super(
            "Failed to create an instance of class '%s'. Ensure it has a public no-arg/all-args constructor."
                .formatted(clazz.getName()),
            cause
        );
    }
}
