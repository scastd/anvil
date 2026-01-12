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
import io.github.anvil.annotations.numeric.GreaterOrEqual;
import io.github.anvil.validation.ValidationError;
import org.junit.jupiter.api.Test;

import java.lang.annotation.Annotation;

import static io.github.anvil.utils.ReflectionUtils.getFieldAnnotation;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class GreaterOrEqualValidatorTest {
    private final GreaterOrEqualValidator validator = new GreaterOrEqualValidator();

    @Validate
    @SuppressWarnings("unused")
    static class GreaterOrEqualTestSchema implements Schema {
        @GreaterOrEqual(0.0f)
        Integer nonNegative;

        @GreaterOrEqual(18.0f)
        Integer age;
    }

    @Test
    void testValidateValidValue() throws ValidationError {
        Annotation annotation = getFieldAnnotation(GreaterOrEqualTestSchema.class, "nonNegative", GreaterOrEqual.class);
        Object returnedValue = this.validator.validate(5, "nonNegative", annotation);
        assertThat(returnedValue).isNull();
    }

    @Test
    void testValidateEqualValue() throws ValidationError {
        Annotation annotation = getFieldAnnotation(GreaterOrEqualTestSchema.class, "nonNegative", GreaterOrEqual.class);
        Object returnedValue = this.validator.validate(0, "nonNegative", annotation);
        assertThat(returnedValue).isNull();
    }

    @Test
    void testValidateInvalidValue() {
        Annotation annotation = getFieldAnnotation(GreaterOrEqualTestSchema.class, "nonNegative", GreaterOrEqual.class);
        assertThatThrownBy(() -> this.validator.validate(-1, "nonNegative", annotation))
            .isInstanceOf(ValidationError.class)
            .hasMessage("for field 'nonNegative': Must be greater than or equal to the specified value (0.0).");
    }

    @Test
    void testValidateNonNumericValue() {
        Annotation annotation = getFieldAnnotation(GreaterOrEqualTestSchema.class, "nonNegative", GreaterOrEqual.class);
        assertThatThrownBy(() -> this.validator.validate("not a number", "nonNegative", annotation))
            .isInstanceOf(ValidationError.class)
            .hasMessage("for field 'nonNegative': Is not a number.");
    }

    @Test
    void testValidateValidAgeValue() throws ValidationError {
        Annotation annotation = getFieldAnnotation(GreaterOrEqualTestSchema.class, "age", GreaterOrEqual.class);
        Object returnedValue = this.validator.validate(25, "age", annotation);
        assertThat(returnedValue).isNull();
    }

    @Test
    void testValidateEqualAgeValue() throws ValidationError {
        Annotation annotation = getFieldAnnotation(GreaterOrEqualTestSchema.class, "age", GreaterOrEqual.class);
        Object returnedValue = this.validator.validate(18, "age", annotation);
        assertThat(returnedValue).isNull();
    }

    @Test
    void testValidateInvalidAgeValue() {
        Annotation annotation = getFieldAnnotation(GreaterOrEqualTestSchema.class, "age", GreaterOrEqual.class);
        assertThatThrownBy(() -> this.validator.validate(17, "age", annotation))
            .isInstanceOf(ValidationError.class)
            .hasMessage("for field 'age': Must be greater than or equal to the specified value (18.0).");
    }

    @Test
    void testGetSupportedAnnotation() {
        assertThat(this.validator.getSupportedAnnotation()).isEqualTo(GreaterOrEqual.class);
    }
}

