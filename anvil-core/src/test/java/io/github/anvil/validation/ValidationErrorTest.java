package io.github.anvil.validation;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ValidationErrorTest {

    @Test
    void testConstructorThrowsOnNullMessage() {
        assertThatThrownBy(() -> new ValidationError(null))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("Validation error message cannot be null");
    }

    @Test
    void testConstructorThrowsOnEmptyString() {
        assertThatThrownBy(() -> new ValidationError(""))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("Validation error message cannot be blank");
    }

    @Test
    void testConstructorThrowsOnBlankString() {
        assertThatThrownBy(() -> new ValidationError("   "))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("Validation error message cannot be blank");
    }
}
