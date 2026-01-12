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

import io.github.anvil.Schema;
import io.github.anvil.annotations.Validate;
import io.github.anvil.annotations.numeric.Equal;
import io.github.anvil.validation.ValidationError;
import org.junit.jupiter.api.Test;

import java.lang.annotation.Annotation;

import static io.github.anvil.utils.ReflectionUtils.getFieldAnnotation;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class EqualValidatorTest {
    private final EqualValidator validator = new EqualValidator();

    @Validate
    @SuppressWarnings("unused")
    static class EqualTestSchema implements Schema {
        @Equal(42.0)
        Integer integerField;

        @Equal(3.14)
        Double doubleField;
    }

    @Test
    void testValidateValidValue() throws ValidationError {
        Annotation annotation = getFieldAnnotation(EqualTestSchema.class, "integerField", Equal.class);
        Object returnedValue = this.validator.validate(42, "integerField", annotation);
        assertThat(returnedValue).isNull();
    }

    @Test
    void testValidateInvalidValueGreaterThan() {
        Annotation annotation = getFieldAnnotation(EqualTestSchema.class, "integerField", Equal.class);
        assertThatThrownBy(() -> this.validator.validate(43, "integerField", annotation))
            .isInstanceOf(ValidationError.class)
            .hasMessage("for field 'integerField': Must be equal to the specified value (42.0).");
    }

    @Test
    void testValidateInvalidValueLessThan() {
        Annotation annotation = getFieldAnnotation(EqualTestSchema.class, "integerField", Equal.class);
        assertThatThrownBy(() -> this.validator.validate(41, "integerField", annotation))
            .isInstanceOf(ValidationError.class)
            .hasMessage("for field 'integerField': Must be equal to the specified value (42.0).");
    }

    @Test
    void testValidateNonNumericValue() {
        Annotation annotation = getFieldAnnotation(EqualTestSchema.class, "integerField", Equal.class);
        assertThatThrownBy(() -> this.validator.validate("not a number", "integerField", annotation))
            .isInstanceOf(ValidationError.class)
            .hasMessage("for field 'integerField': Is not a number.");
    }

    @Test
    void testValidateValidDoubleValue() throws ValidationError {
        Annotation annotation = getFieldAnnotation(EqualTestSchema.class, "doubleField", Equal.class);
        Object returnedValue = this.validator.validate(3.14, "doubleField", annotation);
        assertThat(returnedValue).isNull();
    }

    @Test
    void testValidateInvalidDoubleValue() {
        Annotation annotation = getFieldAnnotation(EqualTestSchema.class, "doubleField", Equal.class);
        assertThatThrownBy(() -> this.validator.validate(3.15, "doubleField", annotation))
            .isInstanceOf(ValidationError.class)
            .hasMessage("for field 'doubleField': Must be equal to the specified value (3.14).");
    }

    @Test
    void testGetSupportedAnnotation() {
        assertThat(this.validator.getSupportedAnnotation()).isEqualTo(Equal.class);
    }
}

