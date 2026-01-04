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
import io.github.anvil.annotations.numeric.Less;
import io.github.anvil.validation.ValidationError;
import org.junit.jupiter.api.Test;

import java.lang.annotation.Annotation;

import static io.github.anvil.utils.ReflectionUtils.getFieldAnnotation;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class LessValidatorTest {
    private final LessValidator validator = new LessValidator();

    @Validate
    @SuppressWarnings("unused")
    static class LessTestSchema implements Schema {
        @Less(100.0f)
        Integer score;

        @Less(10.0f)
        Double value;
    }

    @Test
    void testValidateValidValue() throws ValidationError {
        Annotation annotation = getFieldAnnotation(LessTestSchema.class, "score", Less.class);
        Object returnedValue = this.validator.validate(50, "score", annotation);
        assertThat(returnedValue).isNull();
    }

    @Test
    void testValidateInvalidEqualValue() {
        Annotation annotation = getFieldAnnotation(LessTestSchema.class, "score", Less.class);
        assertThatThrownBy(() -> this.validator.validate(100, "score", annotation))
            .isInstanceOf(ValidationError.class)
            .hasMessage("Field 'score' must be less than the specified value (100.0).");
    }

    @Test
    void testValidateNonNumericValue() {
        Annotation annotation = getFieldAnnotation(LessTestSchema.class, "score", Less.class);
        assertThatThrownBy(() -> this.validator.validate("not a number", "score", annotation))
            .isInstanceOf(ValidationError.class)
            .hasMessage("Field 'score' is not a number.");
    }

    @Test
    void testValidateValidDoubleValue() throws ValidationError {
        Annotation annotation = getFieldAnnotation(LessTestSchema.class, "value", Less.class);
        Object returnedValue = this.validator.validate(5.0, "value", annotation);
        assertThat(returnedValue).isNull();
    }

    @Test
    void testValidateInvalidEqualDoubleValue() {
        Annotation annotation = getFieldAnnotation(LessTestSchema.class, "value", Less.class);
        assertThatThrownBy(() -> this.validator.validate(10.0, "value", annotation))
            .isInstanceOf(ValidationError.class)
            .hasMessage("Field 'value' must be less than the specified value (10.0).");
    }

    @Test
    void testGetSupportedAnnotation() {
        assertThat(this.validator.getSupportedAnnotation()).isEqualTo(Less.class);
    }
}

