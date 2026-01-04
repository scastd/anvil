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
