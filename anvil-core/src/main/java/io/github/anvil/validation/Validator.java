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

package io.github.anvil.validation;

import java.lang.annotation.Annotation;

/**
 * Contract for all annotation-based validators used by Anvil.
 */
public interface Validator {

    /**
     * Validates the given value for the specified field using the provided annotation.
     *
     * @param value      the value to validate.
     * @param fieldName  the name of the field being validated.
     * @param annotation the annotation that defines the validation rule.
     * @return an optional transformed value (it may be {@code null}) to be used instead of the original.
     * @throws ValidationError if validation fails.
     */
    Object validate(Object value, String fieldName, Annotation annotation) throws ValidationError;

    /**
     * Returns the annotation type this validator supports.
     *
     * @return the supported annotation type.
     */
    Class<? extends Annotation> getSupportedAnnotation();
}
