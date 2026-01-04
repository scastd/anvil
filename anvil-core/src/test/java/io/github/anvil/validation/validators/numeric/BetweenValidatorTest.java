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
import io.github.anvil.annotations.numeric.Between;
import io.github.anvil.validation.ValidationError;
import org.junit.jupiter.api.Test;

import java.lang.annotation.Annotation;

import static io.github.anvil.utils.ReflectionUtils.getFieldAnnotation;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class BetweenValidatorTest {
    private final BetweenValidator validator = new BetweenValidator();

    @Validate
    @SuppressWarnings("unused")
    static class BetweenTestSchema implements Schema {
        @Between(min = 0.0f, max = 100.0f)
        Integer percentage;

        @Between(min = -10.0f, max = 10.0f)
        Double temperature;
    }

    @Test
    void testValidateValidValue() throws ValidationError {
        Annotation annotation = getFieldAnnotation(BetweenTestSchema.class, "percentage", Between.class);
        Object returnedValue = this.validator.validate(50, "percentage", annotation);
        assertThat(returnedValue).isNull();
    }

    @Test
    void testValidateMinBoundaryInclusive() throws ValidationError {
        Annotation annotation = getFieldAnnotation(BetweenTestSchema.class, "percentage", Between.class);
        Object returnedValue = this.validator.validate(0, "percentage", annotation);
        assertThat(returnedValue).isNull();
    }

    @Test
    void testValidateMaxBoundaryExclusive() {
        Annotation annotation = getFieldAnnotation(BetweenTestSchema.class, "percentage", Between.class);
        assertThatThrownBy(() -> this.validator.validate(100, "percentage", annotation))
            .isInstanceOf(ValidationError.class)
            .hasMessage("Field 'percentage' must be between 0.0 and 100.0 (not inclusive), but found 100.0");
    }

    @Test
    void testValidateBelowMin() {
        Annotation annotation = getFieldAnnotation(BetweenTestSchema.class, "percentage", Between.class);
        assertThatThrownBy(() -> this.validator.validate(-1, "percentage", annotation))
            .isInstanceOf(ValidationError.class)
            .hasMessage("Field 'percentage' must be between 0.0 and 100.0 (not inclusive), but found -1.0");
    }

    @Test
    void testValidateAboveMax() {
        Annotation annotation = getFieldAnnotation(BetweenTestSchema.class, "percentage", Between.class);
        assertThatThrownBy(() -> this.validator.validate(150, "percentage", annotation))
            .isInstanceOf(ValidationError.class)
            .hasMessage("Field 'percentage' must be between 0.0 and 100.0 (not inclusive), but found 150.0");
    }

    @Test
    void testValidateNonNumericValue() {
        Annotation annotation = getFieldAnnotation(BetweenTestSchema.class, "percentage", Between.class);
        assertThatThrownBy(() -> this.validator.validate("not a number", "percentage", annotation))
            .isInstanceOf(ValidationError.class)
            .hasMessage("Field 'percentage' is not a number.");
    }

    @Test
    void testValidateValidDoubleValue() throws ValidationError {
        Annotation annotation = getFieldAnnotation(BetweenTestSchema.class, "temperature", Between.class);
        Object returnedValue = this.validator.validate(5.5, "temperature", annotation);
        assertThat(returnedValue).isNull();
    }

    @Test
    void testValidateNegativeValue() throws ValidationError {
        Annotation annotation = getFieldAnnotation(BetweenTestSchema.class, "temperature", Between.class);
        Object returnedValue = this.validator.validate(-5.0, "temperature", annotation);
        assertThat(returnedValue).isNull();
    }

    @Test
    void testGetSupportedAnnotation() {
        assertThat(this.validator.getSupportedAnnotation()).isEqualTo(Between.class);
    }
}

