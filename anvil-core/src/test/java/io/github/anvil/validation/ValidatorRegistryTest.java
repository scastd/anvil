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

import org.junit.jupiter.api.Test;

import java.lang.annotation.Annotation;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ValidatorRegistryTest {
    private final ValidatorRegistry validatorRegistry = ValidatorRegistry.getInstance();

    @Test
    void testGetValidatorThrowsWhenNotFound() {
        assertThatThrownBy(() -> validatorRegistry.getValidator(Deprecated.class))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("No validator found for annotation: java.lang.Deprecated");
    }

    @Test
    void testAddValidator() {
        Validator validator = new Validator() {
            @Override
            public Object validate(Object value, String fieldName, Annotation annotation) {
                return null;
            }

            @Override
            public Class<? extends Annotation> getSupportedAnnotation() {
                return Deprecated.class;
            }
        };

        validatorRegistry.addValidator(validator);
        Validator retrievedValidator = validatorRegistry.getValidator(Deprecated.class);

        assertThat(retrievedValidator).isEqualTo(validator);
    }

    @Test
    void testAddNonOverridingValidatorWhenNoValidatorExists() {
        Validator validator = new Validator() {
            @Override
            public Object validate(Object value, String fieldName, Annotation annotation) {
                return null;
            }

            @Override
            public Class<? extends Annotation> getSupportedAnnotation() {
                return SuppressWarnings.class;
            }
        };

        validatorRegistry.addNonOverridingValidator(validator);
        Validator retrievedValidator = validatorRegistry.getValidator(SuppressWarnings.class);

        assertThat(retrievedValidator).isEqualTo(validator);
    }

    @Test
    void testAddNonOverridingValidatorDoesNotOverrideExisting() {
        Validator firstValidator = new Validator() {
            @Override
            public Object validate(Object value, String fieldName, Annotation annotation) {
                return null;
            }

            @Override
            public Class<? extends Annotation> getSupportedAnnotation() {
                return Override.class;
            }
        };

        Validator secondValidator = new Validator() {
            @Override
            public Object validate(Object value, String fieldName, Annotation annotation) {
                return null;
            }

            @Override
            public Class<? extends Annotation> getSupportedAnnotation() {
                return Override.class;
            }
        };

        validatorRegistry.addValidator(firstValidator);
        validatorRegistry.addNonOverridingValidator(secondValidator);

        Validator retrievedValidator = validatorRegistry.getValidator(Override.class);
        assertThat(retrievedValidator).isEqualTo(firstValidator);
        assertThat(retrievedValidator).isNotEqualTo(secondValidator);
    }
}
