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

import io.github.anvil.Schema;
import io.github.anvil.annotations.OptionalValue;
import io.github.anvil.annotations.Validate;
import io.github.anvil.validation.ValidationError;
import org.junit.jupiter.api.Test;

import java.lang.annotation.Annotation;

import static io.github.anvil.utils.ReflectionUtils.getFieldAnnotation;
import static org.assertj.core.api.Assertions.assertThat;

class OptionalValueValidatorTest {
    private final OptionalValueValidator validator = new OptionalValueValidator();

    @Validate
    @SuppressWarnings("unused")
    static class OptionalValueTestSchema implements Schema {
        @OptionalValue
        String optionalField;
    }

    @Test
    void testValidateNullValue() throws ValidationError {
        Annotation annotation = getFieldAnnotation(OptionalValueTestSchema.class, "optionalField", OptionalValue.class);
        Object returnedValue = this.validator.validate(null, "optionalField", annotation);
        assertThat(returnedValue).isNull();
    }

    @Test
    void testValidateNonNullValue() throws ValidationError {
        Annotation annotation = getFieldAnnotation(OptionalValueTestSchema.class, "optionalField", OptionalValue.class);
        Object returnedValue = this.validator.validate("value", "optionalField", annotation);
        assertThat(returnedValue).isEqualTo("value");
    }

    @Test
    void testGetSupportedAnnotation() {
        assertThat(this.validator.getSupportedAnnotation()).isEqualTo(OptionalValue.class);
    }
}
