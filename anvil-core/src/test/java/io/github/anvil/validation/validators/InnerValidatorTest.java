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
import io.github.anvil.annotations.Inner;
import io.github.anvil.annotations.Validate;
import io.github.anvil.exceptions.ValidationException;
import io.github.anvil.processor.Processor;
import io.github.anvil.validation.ValidationError;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.annotation.Annotation;
import java.util.List;

import static io.github.anvil.utils.ReflectionUtils.getFieldAnnotation;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class InnerValidatorTest {

    @Mock
    private Processor<Object> processor;

    private InnerValidator validator;

    @Validate
    @SuppressWarnings("unused")
    static class NestedSchema implements Schema {
        private String field;
    }

    @Validate
    @SuppressWarnings("unused")
    static class ParentSchema implements Schema {
        @Inner(NestedSchema.class)
        NestedSchema nested;
    }

    @BeforeEach
    void setUp() {
        validator = new InnerValidator(processor);
    }

    @Test
    void testGetSupportedAnnotation() {
        assertThat(validator.getSupportedAnnotation()).isEqualTo(Inner.class);
    }

    @Test
    void testValidateSuccessfulValidation() throws ValidationError {
        Object inputValue = new Object();
        NestedSchema expectedSchema = new NestedSchema();
        Annotation annotation = getFieldAnnotation(ParentSchema.class, "nested", Inner.class);

        when(processor.processUnchecked(inputValue, NestedSchema.class))
            .thenReturn(expectedSchema);

        Schema result = validator.validate(inputValue, "nested", annotation);

        assertThat(result).isEqualTo(expectedSchema);
    }

    @Test
    void testValidateThrowsValidationExceptionWithSingleError() {
        Object inputValue = new Object();
        Annotation annotation = getFieldAnnotation(ParentSchema.class, "nested", Inner.class);
        ValidationError error = new ValidationError("for field 'field': Error message");

        when(processor.processUnchecked(inputValue, NestedSchema.class))
            .thenThrow(new ValidationException(List.of(error)));

        assertThatThrownBy(() -> validator.validate(inputValue, "nested", annotation))
            .isInstanceOf(ValidationException.class)
            .satisfies(exception -> {
                ValidationException ve = (ValidationException) exception;
                assertThat(ve.getErrors()).hasSize(1);
                assertThat(ve.getErrors().getFirst().getMessage())
                    .isEqualTo("for field 'nested.field': Error message");
            });
    }

    @Test
    void testValidateThrowsValidationExceptionWithMultipleErrors() {
        Object inputValue = new Object();
        Annotation annotation = getFieldAnnotation(ParentSchema.class, "nested", Inner.class);
        ValidationError error1 = new ValidationError("for field 'field1': First error");
        ValidationError error2 = new ValidationError("for field 'field2': Second error");
        ValidationError error3 = new ValidationError("for field 'field3': Third error");

        when(processor.processUnchecked(inputValue, NestedSchema.class))
            .thenThrow(new ValidationException(List.of(error1, error2, error3)));

        assertThatThrownBy(() -> validator.validate(inputValue, "nested", annotation))
            .isInstanceOf(ValidationException.class)
            .satisfies(exception -> {
                ValidationException ve = (ValidationException) exception;
                assertThat(ve.getErrors()).hasSize(3);
                assertThat(ve.getErrors().get(0).getMessage())
                    .isEqualTo("for field 'nested.field1': First error");
                assertThat(ve.getErrors().get(1).getMessage())
                    .isEqualTo("for field 'nested.field2': Second error");
                assertThat(ve.getErrors().get(2).getMessage())
                    .isEqualTo("for field 'nested.field3': Third error");
            });
    }

    @Test
    void testValidateErrorPrefixingWithNonStandardFormat() {
        Object inputValue = new Object();
        Annotation annotation = getFieldAnnotation(ParentSchema.class, "nested", Inner.class);
        ValidationError error = new ValidationError("Custom error message format");

        when(processor.processUnchecked(inputValue, NestedSchema.class))
            .thenThrow(new ValidationException(List.of(error)));

        assertThatThrownBy(() -> validator.validate(inputValue, "nested", annotation))
            .isInstanceOf(ValidationException.class)
            .satisfies(exception -> {
                ValidationException ve = (ValidationException) exception;
                assertThat(ve.getErrors()).hasSize(1);
                assertThat(ve.getErrors().getFirst().getMessage())
                    .isEqualTo("nested: Custom error message format");
            });
    }

    @Test
    void testValidateErrorPrefixingWithAlreadyPrefixedPath() {
        Object inputValue = new Object();
        Annotation annotation = getFieldAnnotation(ParentSchema.class, "nested", Inner.class);
        ValidationError error = new ValidationError("for field 'parent.child.field': Error message");

        when(processor.processUnchecked(inputValue, NestedSchema.class))
            .thenThrow(new ValidationException(List.of(error)));

        assertThatThrownBy(() -> validator.validate(inputValue, "nested", annotation))
            .isInstanceOf(ValidationException.class)
            .satisfies(exception -> {
                ValidationException ve = (ValidationException) exception;
                assertThat(ve.getErrors()).hasSize(1);
                assertThat(ve.getErrors().getFirst().getMessage())
                    .isEqualTo("for field 'nested.parent.child.field': Error message");
            });
    }
}
