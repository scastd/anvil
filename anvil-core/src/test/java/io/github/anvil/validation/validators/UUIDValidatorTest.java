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
import io.github.anvil.annotations.UUID;
import io.github.anvil.annotations.Validate;
import io.github.anvil.validation.ValidationError;
import org.junit.jupiter.api.Test;

import java.lang.annotation.Annotation;

import static io.github.anvil.utils.ReflectionUtils.getFieldAnnotation;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class UUIDValidatorTest {
    private final UUIDValidator validator = new UUIDValidator();

    @Validate
    @SuppressWarnings("unused")
    static class UuidTestSchema implements Schema {
        @UUID
        String uuidField;
    }

    @Test
    void testValidateValidUuid() throws ValidationError {
        Annotation annotation = getFieldAnnotation(UuidTestSchema.class, "uuidField", UUID.class);
        Object returnedValue = this.validator.validate("550e8400-e29b-41d4-a716-446655440000", "uuidField", annotation);
        assertThat(returnedValue).isNull();
    }

    @Test
    void testValidateValidUuidUppercase() throws ValidationError {
        Annotation annotation = getFieldAnnotation(UuidTestSchema.class, "uuidField", UUID.class);
        Object returnedValue = this.validator.validate("550E8400-E29B-41D4-A716-446655440000", "uuidField", annotation);
        assertThat(returnedValue).isNull();
    }

    @Test
    void testValidateValidUuidMixedCase() throws ValidationError {
        Annotation annotation = getFieldAnnotation(UuidTestSchema.class, "uuidField", UUID.class);
        Object returnedValue = this.validator.validate("550e8400-E29b-41d4-A716-446655440000", "uuidField", annotation);
        assertThat(returnedValue).isNull();
    }

    @Test
    void testValidateNullValue() {
        Annotation annotation = getFieldAnnotation(UuidTestSchema.class, "uuidField", UUID.class);
        assertThatThrownBy(() -> this.validator.validate(null, "uuidField", annotation))
            .isInstanceOf(ValidationError.class)
            .hasMessage("for field 'uuidField': Value is null, but expected a valid UUID.");
    }

    @Test
    void testValidateEmptyString() {
        Annotation annotation = getFieldAnnotation(UuidTestSchema.class, "uuidField", UUID.class);
        assertThatThrownBy(() -> this.validator.validate("", "uuidField", annotation))
            .isInstanceOf(ValidationError.class)
            .hasMessage("for field 'uuidField': Value '' is not a valid UUID format.");
    }

    @Test
    void testValidateInvalidUuidMissingHyphens() {
        Annotation annotation = getFieldAnnotation(UuidTestSchema.class, "uuidField", UUID.class);
        assertThatThrownBy(() -> this.validator.validate("550e8400e29b41d4a716446655440000", "uuidField", annotation))
            .isInstanceOf(ValidationError.class)
            .hasMessage("for field 'uuidField': Value '550e8400e29b41d4a716446655440000' is not a valid UUID format.");
    }

    @Test
    void testValidateInvalidUuidInvalidCharacters() {
        Annotation annotation = getFieldAnnotation(UuidTestSchema.class, "uuidField", UUID.class);
        assertThatThrownBy(
            () -> this.validator.validate("550e8400-e29b-41d4-a716-44665544000g", "uuidField", annotation))
            .isInstanceOf(ValidationError.class)
            .hasMessage(
                "for field 'uuidField': Value '550e8400-e29b-41d4-a716-44665544000g' is not a valid UUID format.");
    }

    @Test
    void testValidateInvalidUuidWrongFormat() {
        Annotation annotation = getFieldAnnotation(UuidTestSchema.class, "uuidField", UUID.class);
        assertThatThrownBy(() -> this.validator.validate("not-a-uuid", "uuidField", annotation))
            .isInstanceOf(ValidationError.class)
            .hasMessage("for field 'uuidField': Value 'not-a-uuid' is not a valid UUID format.");
    }

    @Test
    void testValidateInvalidUuidTooManyHyphens() {
        Annotation annotation = getFieldAnnotation(UuidTestSchema.class, "uuidField", UUID.class);
        assertThatThrownBy(
            () -> this.validator.validate("550e8400-e29b-41d4-a716-4466-55440000", "uuidField", annotation))
            .isInstanceOf(ValidationError.class)
            .hasMessage(
                "for field 'uuidField': Value '550e8400-e29b-41d4-a716-4466-55440000' is not a valid UUID format.");
    }

    @Test
    void testValidateInvalidUuidTooFewHyphens() {
        Annotation annotation = getFieldAnnotation(UuidTestSchema.class, "uuidField", UUID.class);
        assertThatThrownBy(
            () -> this.validator.validate("550e8400-e29b-41d4-a716446655440000", "uuidField", annotation))
            .isInstanceOf(ValidationError.class)
            .hasMessage(
                "for field 'uuidField': Value '550e8400-e29b-41d4-a716446655440000' is not a valid UUID format.");
    }

    @Test
    void testGetSupportedAnnotation() {
        assertThat(this.validator.getSupportedAnnotation()).isEqualTo(UUID.class);
    }
}
