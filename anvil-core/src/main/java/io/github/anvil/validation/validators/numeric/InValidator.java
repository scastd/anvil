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

import io.github.anvil.annotations.numeric.In;
import io.github.anvil.validation.ValidationError;

import java.lang.annotation.Annotation;
import java.util.Arrays;

/**
 * Validator for the {@link In} annotation, enforcing membership in a set of valid values.
 */
public class InValidator extends NumericValidator {

    /**
     * Validates that the given numeric value is contained in the configured list of valid values.
     *
     * @param value      the value to validate.
     * @param fieldName  the name of the field being validated.
     * @param annotation the {@link In} annotation instance.
     * @return {@code null} if validation succeeds.
     * @throws ValidationError if the configured list is empty or the value is not contained in it.
     */
    @Override
    public Object validate(Object value, String fieldName, Annotation annotation) throws ValidationError {
        In in = (In) annotation;
        double[] validValues = in.value();
        double number = this.getNumber(value, fieldName).doubleValue();

        if (validValues.length == 0) {
            throw new ValidationError(
                "for field '%s': At least one valid value must be specified for the '@In' validation.".formatted(
                    fieldName));
        }

        for (double validValue : validValues) {
            if (number == validValue) {
                return null;
            }
        }

        throw new ValidationError(
            "for field '%s': Found value '%s', but expected one of: %s.".formatted(fieldName, number,
                                                                                  Arrays.toString(validValues)));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Class<? extends Annotation> getSupportedAnnotation() {
        return In.class;
    }
}
