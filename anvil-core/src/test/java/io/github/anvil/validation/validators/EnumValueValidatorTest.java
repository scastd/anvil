package io.github.anvil.validation.validators;

import io.github.anvil.Schema;
import io.github.anvil.annotations.EnumValue;
import io.github.anvil.annotations.Validate;
import io.github.anvil.validation.ValidationError;
import org.junit.jupiter.api.Test;

import java.lang.annotation.Annotation;

import static io.github.anvil.utils.ReflectionUtils.getFieldAnnotation;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class EnumValueValidatorTest {
    private final EnumValueValidator validator = new EnumValueValidator();

    enum Color {
        RED,
        GREEN,
        BLUE
    }

    @Validate
    @SuppressWarnings("unused")
    static class EnumValueTestSchema implements Schema {
        @EnumValue(Color.class)
        String color;
    }

    @Test
    void testValidateValidEnumValue() throws ValidationError {
        Annotation annotation = getFieldAnnotation(EnumValueTestSchema.class, "color", EnumValue.class);
        Object returnedValue = this.validator.validate("RED", "color", annotation);
        assertEquals(Color.RED, returnedValue);
    }

    @Test
    void testValidateInvalidEnumValue() {
        Annotation annotation = getFieldAnnotation(EnumValueTestSchema.class, "color", EnumValue.class);
        ValidationError error = assertThrows(ValidationError.class,
                                             () -> this.validator.validate("YELLOW", "color", annotation));
        assertEquals(
            "Field 'color' has value 'YELLOW' which is not among the allowed enum values for the enum 'Color': [RED, GREEN, BLUE]",
            error.getMessage());
    }

    @Test
    void testValidateInvalidEnumValueCaseSensitive() {
        Annotation annotation = getFieldAnnotation(EnumValueTestSchema.class, "color", EnumValue.class);
        ValidationError error = assertThrows(ValidationError.class,
                                             () -> this.validator.validate("red", "color", annotation));
        assertEquals(
            "Field 'color' has value 'red' which is not among the allowed enum values for the enum 'Color': [RED, GREEN, BLUE]",
            error.getMessage());
    }

    @Test
    void testValidateNonStringValue() {
        Annotation annotation = getFieldAnnotation(EnumValueTestSchema.class, "color", EnumValue.class);
        ValidationError error = assertThrows(ValidationError.class,
                                             () -> this.validator.validate(123, "color", annotation));
        assertEquals("Field 'color' is not a string (123), cannot validate enum value.",
                     error.getMessage());
    }

    @Test
    void testGetSupportedAnnotation() {
        assertEquals(EnumValue.class, this.validator.getSupportedAnnotation());
    }
}

