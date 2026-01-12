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

import io.github.anvil.annotations.StrIn;
import io.github.anvil.validation.ValidationError;
import io.github.anvil.validation.Validator;

import java.lang.annotation.Annotation;
import java.util.Arrays;

/**
 * Validator for the {@link StrIn} annotation, enforcing membership in a set of allowed strings.
 */
public class StrInValidator implements Validator {

    /**
     * Validates that the given string value is one of the configured {@link StrIn#value()} options,
     * using the specified {@link StrIn#strategy()}.
     *
     * @param value      the value to validate.
     * @param fieldName  the name of the field being validated.
     * @param annotation the {@link StrIn} annotation instance.
     * @return {@code null} if validation succeeds.
     * @throws ValidationError if the value is not in the allowed set.
     */
    @Override
    public Object validate(Object value, String fieldName, Annotation annotation) throws ValidationError {
        StrIn strIn = (StrIn) annotation;
        String[] validValues = strIn.value();

        for (String validValue : validValues) {
            if (StringComparer.equal((String) value, validValue, strIn.strategy())) {
                return null;
            }
        }

        throw new ValidationError(
            "for field '%s': Value '%s' is not in the allowed set: %s".formatted(fieldName, value,
                                                                                 Arrays.toString(validValues))
        );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Class<? extends Annotation> getSupportedAnnotation() {
        return StrIn.class;
    }
}
