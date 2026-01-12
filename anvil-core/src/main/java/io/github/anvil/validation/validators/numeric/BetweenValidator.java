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

import io.github.anvil.annotations.numeric.Between;
import io.github.anvil.validation.ValidationError;

import java.lang.annotation.Annotation;

/**
 * Validator for the {@link Between} annotation, enforcing a half-open interval {@code [min, max)}.
 */
public class BetweenValidator extends NumericValidator {

    /**
     * Validates that the given numeric value lies within the configured {@link Between} range.
     *
     * @param value      the value to validate.
     * @param fieldName  the name of the field being validated.
     * @param annotation the {@link Between} annotation instance.
     * @return {@code null} if validation succeeds.
     * @throws ValidationError if the value is outside the allowed range.
     */
    @Override
    public Object validate(Object value, String fieldName, Annotation annotation) throws ValidationError {
        Between between = (Between) annotation;
        double number = this.getNumber(value, fieldName).doubleValue();

        if (number < between.min() || number >= between.max()) {
            throw new ValidationError(
                "for field '%s': Must be between %s and %s (not inclusive), but found %s.".formatted(
                    fieldName,
                    between.min(),
                    between.max(),
                    number
                )
            );
        }

        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Class<? extends Annotation> getSupportedAnnotation() {
        return Between.class;
    }
}
