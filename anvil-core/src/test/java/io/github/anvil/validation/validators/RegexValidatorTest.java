package io.github.anvil.validation.validators;

import io.github.anvil.Schema;
import io.github.anvil.annotations.Regex;
import io.github.anvil.annotations.Validate;
import io.github.anvil.validation.ValidationError;
import org.junit.jupiter.api.Test;

import java.lang.annotation.Annotation;

import static io.github.anvil.utils.ReflectionUtils.getFieldAnnotation;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

class RegexValidatorTest {
    private final RegexValidator validator = new RegexValidator();

    @Validate
    @SuppressWarnings("unused")
    static class RegexTestSchema implements Schema {
        @Regex("[a-z]+")
        String lowercaseField;

        @Regex("\\d{3}-\\d{4}")
        String phoneField;

        @Regex("^[A-Z][a-z]*$")
        String capitalizedField;
    }

    @Test
    void testValidateValidValue() throws ValidationError {
        Annotation annotation = getFieldAnnotation(RegexTestSchema.class, "lowercaseField", Regex.class);
        Object returnedValue = this.validator.validate("abc", "lowercaseField", annotation);
        assertNull(returnedValue);
    }

    @Test
    void testValidateInvalidValue() {
        Annotation annotation = getFieldAnnotation(RegexTestSchema.class, "lowercaseField", Regex.class);
        ValidationError error = assertThrows(ValidationError.class,
                                             () -> this.validator.validate("ABC", "lowercaseField", annotation));
        assertEquals("Value 'ABC' for field 'lowercaseField' does not match the required pattern: '[a-z]+'.",
                     error.getMessage());
    }

    @Test
    void testValidateNullValue() {
        Annotation annotation = getFieldAnnotation(RegexTestSchema.class, "lowercaseField", Regex.class);
        ValidationError error = assertThrows(ValidationError.class,
                                             () -> this.validator.validate(null, "lowercaseField", annotation));
        assertEquals("Value 'null' for field 'lowercaseField' does not match the required pattern: '[a-z]+'.",
                     error.getMessage());
    }

    @Test
    void testValidateValidPhoneFormat() throws ValidationError {
        Annotation annotation = getFieldAnnotation(RegexTestSchema.class, "phoneField", Regex.class);
        Object returnedValue = this.validator.validate("123-4567", "phoneField", annotation);
        assertNull(returnedValue);
    }

    @Test
    void testValidateInvalidPhoneFormat() {
        Annotation annotation = getFieldAnnotation(RegexTestSchema.class, "phoneField", Regex.class);
        ValidationError error = assertThrows(ValidationError.class,
                                             () -> this.validator.validate("1234567", "phoneField", annotation));
        assertEquals("Value '1234567' for field 'phoneField' does not match the required pattern: '\\d{3}-\\d{4}'.",
                     error.getMessage());
    }

    @Test
    void testValidateValidCapitalizedValue() throws ValidationError {
        Annotation annotation = getFieldAnnotation(RegexTestSchema.class, "capitalizedField", Regex.class);
        Object returnedValue = this.validator.validate("Hello", "capitalizedField", annotation);
        assertNull(returnedValue);
    }

    @Test
    void testValidateInvalidCapitalizedValue() {
        Annotation annotation = getFieldAnnotation(RegexTestSchema.class, "capitalizedField", Regex.class);
        ValidationError error = assertThrows(ValidationError.class,
                                             () -> this.validator.validate("hello", "capitalizedField", annotation));
        assertEquals("Value 'hello' for field 'capitalizedField' does not match the required pattern: '^[A-Z][a-z]*$'.",
                     error.getMessage());
    }

    @Test
    void testGetSupportedAnnotation() {
        assertEquals(Regex.class, this.validator.getSupportedAnnotation());
    }
}

