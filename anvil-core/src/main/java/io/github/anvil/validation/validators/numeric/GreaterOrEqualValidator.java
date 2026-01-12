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

import io.github.anvil.annotations.numeric.GreaterOrEqual;
import io.github.anvil.validation.ValidationError;

import java.lang.annotation.Annotation;

/**
 * Validator for the {@link GreaterOrEqual} annotation, enforcing a greater-than-or-equal-to constraint.
 */
public class GreaterOrEqualValidator extends NumericValidator {

    /**
     * Validates that the given numeric value is greater than or equal to the configured {@link GreaterOrEqual#value()}.
     *
     * @param value      the value to validate.
     * @param fieldName  the name of the field being validated.
     * @param annotation the {@link GreaterOrEqual} annotation instance.
     * @return {@code null} if validation succeeds.
     * @throws ValidationError if the value is less than the threshold.
     */
    @Override
    public Object validate(Object value, String fieldName, Annotation annotation) throws ValidationError {
        GreaterOrEqual greaterOrEqual = (GreaterOrEqual) annotation;
        double number = this.getNumber(value, fieldName).doubleValue();

        if (number < greaterOrEqual.value()) {
            throw new ValidationError(
                "for field '%s': Must be greater than or equal to the specified value (%s).".formatted(fieldName,
                                                                                                  greaterOrEqual.value()));
        }

        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Class<? extends Annotation> getSupportedAnnotation() {
        return GreaterOrEqual.class;
    }
}
