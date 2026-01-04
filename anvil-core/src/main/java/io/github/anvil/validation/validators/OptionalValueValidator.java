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

import io.github.anvil.annotations.OptionalValue;
import io.github.anvil.validation.ValidationError;
import io.github.anvil.validation.Validator;

import java.lang.annotation.Annotation;

/**
 * Validator for the {@link OptionalValue} annotation.
 *
 * <p>This validator does not enforce any additional constraints; it simply passes the
 * value through. The optional behavior is handled at the processor level.</p>
 */
public class OptionalValueValidator implements Validator {

    /**
     * Returns the value as-is, without performing any checks.
     *
     * @param value      the value to validate.
     * @param fieldName  the name of the field being validated.
     * @param annotation the {@link OptionalValue} annotation instance.
     * @return the original value, unchanged.
     * @throws ValidationError never; this validator does not raise validation errors.
     */
    @Override
    public Object validate(Object value, String fieldName, Annotation annotation) throws ValidationError {
        // Since the field is optional, we simply return the value as is (null or non-null)
        return value;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Class<? extends Annotation> getSupportedAnnotation() {
        return OptionalValue.class;
    }
}
