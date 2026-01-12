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

import io.github.anvil.annotations.numeric.Greater;
import io.github.anvil.validation.ValidationError;

import java.lang.annotation.Annotation;

/**
 * Validator for the {@link Greater} annotation, enforcing a strictly greater-than constraint.
 */
public class GreaterValidator extends NumericValidator {

    /**
     * Validates that the given numeric value is strictly greater than the configured {@link Greater#value()}.
     *
     * @param value      the value to validate.
     * @param fieldName  the name of the field being validated.
     * @param annotation the {@link Greater} annotation instance.
     * @return {@code null} if validation succeeds.
     * @throws ValidationError if the value is less than or equal to the threshold.
     */
    @Override
    public Object validate(Object value, String fieldName, Annotation annotation) throws ValidationError {
        Greater greater = (Greater) annotation;
        double number = this.getNumber(value, fieldName).doubleValue();

        if (number <= greater.value()) {
            throw new ValidationError(
                "for field '%s': Must be greater than the specified value (%s).".formatted(fieldName, greater.value()));
        }

        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Class<? extends Annotation> getSupportedAnnotation() {
        return Greater.class;
    }
}
