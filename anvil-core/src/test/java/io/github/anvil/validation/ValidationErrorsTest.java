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
