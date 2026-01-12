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
import io.github.anvil.annotations.List;
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import static io.github.anvil.utils.ReflectionUtils.getFieldAnnotation;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ListValidatorTest {

    @Mock
    private Processor<Object> processor;

    private ListValidator validator;

    @Validate
    @SuppressWarnings("unused")
    static class ItemSchema implements Schema {
        private String name;
        private int quantity;
    }

    @Validate
    @SuppressWarnings("unused")
    static class OrderSchema implements Schema {
        @List(ItemSchema.class)
        private java.util.List<ItemSchema> items;
    }

    @BeforeEach
    void setUp() {
        validator = new ListValidator(processor);
    }

    @Test
    void testGetSupportedAnnotation() {
        assertThat(validator.getSupportedAnnotation()).isEqualTo(List.class);
    }

    @Test
    void testValidateSuccessfulValidationWithList() throws ValidationError {
        Object element1 = new Object();
        Object element2 = new Object();
        ItemSchema item1 = new ItemSchema();
        ItemSchema item2 = new ItemSchema();
        Collection<Object> inputList = Arrays.asList(element1, element2);
        Annotation annotation = getFieldAnnotation(OrderSchema.class, "items", List.class);

        when(processor.processUnchecked(element1, ItemSchema.class)).thenReturn(item1);
        when(processor.processUnchecked(element2, ItemSchema.class)).thenReturn(item2);

        java.util.List<Schema> result = validator.validate(inputList, "items", annotation);

        assertThat(result).hasSize(2);
        assertThat(result).containsExactly(item1, item2);
    }

    @Test
    void testValidateSuccessfulValidationWithArray() throws ValidationError {
        Object element1 = new Object();
        Object element2 = new Object();
        ItemSchema item1 = new ItemSchema();
        ItemSchema item2 = new ItemSchema();
        Object[] inputArray = { element1, element2 };
        Annotation annotation = getFieldAnnotation(OrderSchema.class, "items", List.class);

        when(processor.processUnchecked(element1, ItemSchema.class)).thenReturn(item1);
        when(processor.processUnchecked(element2, ItemSchema.class)).thenReturn(item2);

        java.util.List<Schema> result = validator.validate(inputArray, "items", annotation);

        assertThat(result).hasSize(2);
        assertThat(result).containsExactly(item1, item2);
    }

    @Test
    void testValidateEmptyList() throws ValidationError {
        Collection<Object> emptyList = new ArrayList<>();
        Annotation annotation = getFieldAnnotation(OrderSchema.class, "items", List.class);

        java.util.List<Schema> result = validator.validate(emptyList, "items", annotation);

        assertThat(result).isEmpty();
    }

    @Test
    void testValidateNullValue() {
        Annotation annotation = getFieldAnnotation(OrderSchema.class, "items", List.class);

        assertThatThrownBy(() -> validator.validate(null, "items", annotation))
            .isInstanceOf(ValidationError.class)
            .hasMessage("for field 'items': List value cannot be null.");
    }

    @Test
    void testValidateInvalidType() {
        String invalidValue = "not a collection or array";
        Annotation annotation = getFieldAnnotation(OrderSchema.class, "items", List.class);

        assertThatThrownBy(() -> validator.validate(invalidValue, "items", annotation))
            .isInstanceOf(ValidationError.class)
            .satisfies(error -> {
                assertThat(error.getMessage())
                    .contains("Value must be a Collection or array, but was: java.lang.String");
            });
    }

    @Test
    void testValidateThrowsValidationExceptionWithSingleError() {
        Object element1 = new Object();
        Object element2 = new Object();
        ItemSchema item1 = new ItemSchema();
        Collection<Object> inputList = Arrays.asList(element1, element2);
        Annotation annotation = getFieldAnnotation(OrderSchema.class, "items", List.class);
        ValidationError error = new ValidationError("for field 'name': Error message");

        when(processor.processUnchecked(element1, ItemSchema.class)).thenReturn(item1);
        when(processor.processUnchecked(element2, ItemSchema.class))
            .thenThrow(new ValidationException(java.util.List.of(error)));

        assertThatThrownBy(() -> validator.validate(inputList, "items", annotation))
            .isInstanceOf(ValidationException.class)
            .satisfies(exception -> {
                ValidationException ve = (ValidationException) exception;
                assertThat(ve.getErrors()).hasSize(1);
                assertThat(ve.getErrors().getFirst().getMessage())
                    .isEqualTo("for field 'items[1].name': Error message");
            });
    }

    @Test
    void testValidateThrowsValidationExceptionWithMultipleErrorsInSingleElement() {
        Object element1 = new Object();
        Object element2 = new Object();
        ItemSchema item1 = new ItemSchema();
        Collection<Object> inputList = Arrays.asList(element1, element2);
        Annotation annotation = getFieldAnnotation(OrderSchema.class, "items", List.class);
        ValidationError error1 = new ValidationError("for field 'name': First error");
        ValidationError error2 = new ValidationError("for field 'quantity': Second error");

        when(processor.processUnchecked(element1, ItemSchema.class)).thenReturn(item1);
        when(processor.processUnchecked(element2, ItemSchema.class))
            .thenThrow(new ValidationException(java.util.List.of(error1, error2)));

        assertThatThrownBy(() -> validator.validate(inputList, "items", annotation))
            .isInstanceOf(ValidationException.class)
            .satisfies(exception -> {
                ValidationException ve = (ValidationException) exception;
                assertThat(ve.getErrors()).hasSize(2);
                assertThat(ve.getErrors().get(0).getMessage())
                    .isEqualTo("for field 'items[1].name': First error");
                assertThat(ve.getErrors().get(1).getMessage())
                    .isEqualTo("for field 'items[1].quantity': Second error");
            });
    }

    @Test
    void testValidateThrowsValidationExceptionWithErrorsInMultipleElements() {
        Object element1 = new Object();
        Object element2 = new Object();
        Object element3 = new Object();
        Collection<Object> inputList = Arrays.asList(element1, element2, element3);
        Annotation annotation = getFieldAnnotation(OrderSchema.class, "items", List.class);
        ValidationError error1 = new ValidationError("for field 'name': Error in first element");
        ValidationError error2 = new ValidationError("for field 'quantity': Error in second element");

        when(processor.processUnchecked(element1, ItemSchema.class))
            .thenThrow(new ValidationException(java.util.List.of(error1)));
        when(processor.processUnchecked(element2, ItemSchema.class))
            .thenThrow(new ValidationException(java.util.List.of(error2)));
        when(processor.processUnchecked(element3, ItemSchema.class))
            .thenReturn(new ItemSchema());

        assertThatThrownBy(() -> validator.validate(inputList, "items", annotation))
            .isInstanceOf(ValidationException.class)
            .satisfies(exception -> {
                ValidationException ve = (ValidationException) exception;
                assertThat(ve.getErrors()).hasSize(2);
                assertThat(ve.getErrors().get(0).getMessage())
                    .isEqualTo("for field 'items[0].name': Error in first element");
                assertThat(ve.getErrors().get(1).getMessage())
                    .isEqualTo("for field 'items[1].quantity': Error in second element");
            });
    }

    @Test
    void testValidateErrorPrefixingWithNonStandardFormat() {
        Object element1 = new Object();
        Collection<Object> inputList = Arrays.asList(element1);
        Annotation annotation = getFieldAnnotation(OrderSchema.class, "items", List.class);
        ValidationError error = new ValidationError("Custom error message format");

        when(processor.processUnchecked(element1, ItemSchema.class))
            .thenThrow(new ValidationException(java.util.List.of(error)));

        assertThatThrownBy(() -> validator.validate(inputList, "items", annotation))
            .isInstanceOf(ValidationException.class)
            .satisfies(exception -> {
                ValidationException ve = (ValidationException) exception;
                assertThat(ve.getErrors()).hasSize(1);
                assertThat(ve.getErrors().getFirst().getMessage())
                    .isEqualTo("items[0]: Custom error message format");
            });
    }

    @Test
    void testValidateErrorPrefixingWithAlreadyPrefixedPath() {
        Object element1 = new Object();
        Collection<Object> inputList = Arrays.asList(element1);
        Annotation annotation = getFieldAnnotation(OrderSchema.class, "items", List.class);
        ValidationError error = new ValidationError("for field 'nested.field': Error message");

        when(processor.processUnchecked(element1, ItemSchema.class))
            .thenThrow(new ValidationException(java.util.List.of(error)));

        assertThatThrownBy(() -> validator.validate(inputList, "items", annotation))
            .isInstanceOf(ValidationException.class)
            .satisfies(exception -> {
                ValidationException ve = (ValidationException) exception;
                assertThat(ve.getErrors()).hasSize(1);
                assertThat(ve.getErrors().getFirst().getMessage())
                    .isEqualTo("for field 'items[0].nested.field': Error message");
            });
    }
}
