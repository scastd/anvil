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

        throw new ValidationError("for field '%s': Is not a number.".formatted(fieldName));
    }
}
