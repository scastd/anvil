package io.github.anvil.validation.validators;

import io.github.anvil.Schema;
import io.github.anvil.annotations.Validate;
import io.github.anvil.annotations.ValidateField;
import io.github.anvil.validation.ValidationError;
import org.junit.jupiter.api.Test;

import java.lang.annotation.Annotation;

import static io.github.anvil.utils.ReflectionUtils.getFieldAnnotation;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ValidateFieldValidatorTest {
    private final ValidateFieldValidator validator = new ValidateFieldValidator();

    @Validate
    @SuppressWarnings("unused")
    static class ValidateFieldTestSchema extends Schema {
        @ValidateField
        String requiredField;

        @ValidateField(required = false)
        String optionalField;
    }

    @Test
    void testValidateRequired() throws ValidationError {
        Annotation requiredAnnotation = getFieldAnnotation(ValidateFieldTestSchema.class, "requiredField",
                                                           ValidateField.class);
        Object returnedValue = this.validator.validate(0, "testField", requiredAnnotation);
        assertEquals(0, returnedValue);
    }

    @Test
    void testValidateOptional() throws ValidationError {
        Annotation optionalAnnotation = getFieldAnnotation(ValidateFieldTestSchema.class, "optionalField",
                                                           ValidateField.class);
        Object returnedValue = this.validator.validate(null, "testField", optionalAnnotation);
        assertNull(returnedValue);
    }

    @Test
    void testValidateRequiredThrowsOnNullValue() {
        Annotation requiredAnnotation = getFieldAnnotation(ValidateFieldTestSchema.class, "requiredField",
                                                           ValidateField.class);
        ValidationError error = assertThrows(ValidationError.class,
                                             () -> this.validator.validate(null, "testField", requiredAnnotation));
        assertEquals("Field 'testField' is required but not provided.", error.getMessage());
    }

    @Test
    void testGetSupportedAnnotation() {
        assertEquals(ValidateField.class, this.validator.getSupportedAnnotation());
    }
}
