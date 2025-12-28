package io.github.anvil.validation;

import io.github.anvil.exceptions.ValidationException;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ValidationErrorsTest {

    @Test
    void testAddErrorWhenFailFastFalse() {
        ValidationErrors validationErrors = new ValidationErrors(false);

        validationErrors.addError(new ValidationError("Error 1"));
        validationErrors.addError(new ValidationError("Error 2"));

        assertThatThrownBy(validationErrors::throwIfAny)
            .isInstanceOf(ValidationException.class)
            .hasMessage("Validation failed with 2 error(s):\n\t- Error 1\n\t- Error 2");
    }

    @Test
    void testAddErrorWhenFailFastTrueThrowsImmediately() {
        ValidationErrors validationErrors = new ValidationErrors(true);

        assertThatThrownBy(() -> validationErrors.addError(new ValidationError("First error")))
            .isInstanceOf(ValidationException.class)
            .hasMessage("Validation failed with 1 error(s):\n\t- First error");
    }

    @Test
    void testThrowIfAnyWhenNoErrors() {
        ValidationErrors validationErrors = new ValidationErrors(false);

        assertThatNoException().isThrownBy(validationErrors::throwIfAny);
    }

    @Test
    void testFailFastStopsOnFirstError() {
        ValidationErrors validationErrors = new ValidationErrors(true);

        assertThatThrownBy(() -> {
            validationErrors.addError(new ValidationError("First error"));
            validationErrors.addError(new ValidationError("Second error"));
        })
            .isInstanceOf(ValidationException.class)
            .hasMessage("Validation failed with 1 error(s):\n\t- First error");
    }

    @Test
    void testErrorOrderPreservation() {
        ValidationErrors validationErrors = new ValidationErrors(false);
        ValidationError error1 = new ValidationError("Error A");
        ValidationError error2 = new ValidationError("Error B");
        ValidationError error3 = new ValidationError("Error C");

        validationErrors.addError(error1);
        validationErrors.addError(error2);
        validationErrors.addError(error3);

        assertThatThrownBy(validationErrors::throwIfAny)
            .isInstanceOf(ValidationException.class)
            .hasMessage("Validation failed with 3 error(s):\n\t- Error A\n\t- Error B\n\t- Error C");
    }
}
