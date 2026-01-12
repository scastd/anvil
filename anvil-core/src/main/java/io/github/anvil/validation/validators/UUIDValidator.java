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

package io.github.anvil.validation.validators;

import io.github.anvil.annotations.UUID;
import io.github.anvil.validation.ValidationError;
import io.github.anvil.validation.Validator;

import java.lang.annotation.Annotation;

/**
 * Validator for the {@link UUID} annotation, enforcing that a string value is a valid UUID format.
 */
public class UUIDValidator implements Validator {

    /**
     * Validates that the given string value is a valid UUID format (e.g., "550e8400-e29b-41d4-a716-446655440000").
     *
     * @param value      the value to validate.
     * @param fieldName  the name of the field being validated.
     * @param annotation the {@link UUID} annotation instance.
     * @return {@code null} if validation succeeds.
     * @throws ValidationError if the value is {@code null} or is not a valid UUID format.
     */
    @Override
    public Object validate(Object value, String fieldName, Annotation annotation) throws ValidationError {
        if (value == null) {
            throw new ValidationError(
                "for field '%s': Value is null, but expected a valid UUID.".formatted(fieldName)
            );
        }

        String stringValue = (String) value;

        try {
            java.util.UUID.fromString(stringValue);
        } catch (IllegalArgumentException e) {
            throw new ValidationError(
                "for field '%s': Value '%s' is not a valid UUID format.".formatted(fieldName, stringValue)
            );
        }

        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Class<? extends Annotation> getSupportedAnnotation() {
        return UUID.class;
    }
}
