package io.github.anvil.exceptions;

import io.github.anvil.validation.ValidationError;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ValidationExceptionTest {

    @Test
    void testConstructorWithSingleError() {
        ValidationError error = new ValidationError("Single error message");

        ValidationException exception = new ValidationException(List.of(error));

        assertThat(exception.getErrors()).hasSize(1);
        assertThat(exception.getErrors().getFirst().getMessage()).isEqualTo("Single error message");
        assertThat(exception.getMessage())
            .isEqualTo("Validation failed with 1 error(s):\n\t- Single error message");
    }

    @Test
    void testConstructorWithMultipleErrors() {
        ValidationError error1 = new ValidationError("First error");
        ValidationError error2 = new ValidationError("Second error");
        ValidationError error3 = new ValidationError("Third error");

        ValidationException exception = new ValidationException(List.of(error1, error2, error3));

        assertThat(exception.getErrors()).hasSize(3);
        assertThat(exception.getErrors().get(0).getMessage()).isEqualTo("First error");
        assertThat(exception.getErrors().get(1).getMessage()).isEqualTo("Second error");
        assertThat(exception.getErrors().get(2).getMessage()).isEqualTo("Third error");
        assertThat(exception.getMessage())
            .isEqualTo("Validation failed with 3 error(s):\n\t- First error\n\t- Second error\n\t- Third error");
    }

    @Test
    void testConstructorWithEmptyCollection() {
        assertThatThrownBy(() -> {
            throw new ValidationException(Collections.emptyList());
        })
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("ValidationException requires at least one validation error");
    }

    @Test
    void testConstructorWithNullCollection() {
        assertThatThrownBy(() -> {
            throw new ValidationException(null);
        })
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("ValidationException requires at least one validation error");
    }

    @Test
    void testGetErrorsReturnsUnmodifiableList() {
        ValidationError error = new ValidationError("Test error");
        ValidationException exception = new ValidationException(List.of(error));

        assertThatThrownBy(() -> exception.getErrors().add(new ValidationError("Another error")))
            .isInstanceOf(UnsupportedOperationException.class);
    }
}
