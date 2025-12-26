package io.github.anvil.validation.validators;

import io.github.anvil.Schema;
import io.github.anvil.annotations.Regex;
import io.github.anvil.annotations.Validate;
import io.github.anvil.validation.ValidationError;
import org.junit.jupiter.api.Test;

import java.lang.annotation.Annotation;

import static io.github.anvil.utils.ReflectionUtils.getFieldAnnotation;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

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
        assertThat(returnedValue).isNull();
    }

    @Test
    void testValidateInvalidValue() {
        Annotation annotation = getFieldAnnotation(RegexTestSchema.class, "lowercaseField", Regex.class);
        assertThatThrownBy(() -> this.validator.validate("ABC", "lowercaseField", annotation))
            .isInstanceOf(ValidationError.class)
            .hasMessage("Value 'ABC' for field 'lowercaseField' does not match the required pattern: '[a-z]+'.");
    }

    @Test
    void testValidateNullValue() {
        Annotation annotation = getFieldAnnotation(RegexTestSchema.class, "lowercaseField", Regex.class);
        assertThatThrownBy(() -> this.validator.validate(null, "lowercaseField", annotation))
            .isInstanceOf(ValidationError.class)
            .hasMessage("Value 'null' for field 'lowercaseField' does not match the required pattern: '[a-z]+'.");
    }

    @Test
    void testValidateValidPhoneFormat() throws ValidationError {
        Annotation annotation = getFieldAnnotation(RegexTestSchema.class, "phoneField", Regex.class);
        Object returnedValue = this.validator.validate("123-4567", "phoneField", annotation);
        assertThat(returnedValue).isNull();
    }

    @Test
    void testValidateInvalidPhoneFormat() {
        Annotation annotation = getFieldAnnotation(RegexTestSchema.class, "phoneField", Regex.class);
        assertThatThrownBy(() -> this.validator.validate("1234567", "phoneField", annotation))
            .isInstanceOf(ValidationError.class)
            .hasMessage("Value '1234567' for field 'phoneField' does not match the required pattern: '\\d{3}-\\d{4}'.");
    }

    @Test
    void testValidateValidCapitalizedValue() throws ValidationError {
        Annotation annotation = getFieldAnnotation(RegexTestSchema.class, "capitalizedField", Regex.class);
        Object returnedValue = this.validator.validate("Hello", "capitalizedField", annotation);
        assertThat(returnedValue).isNull();
    }

    @Test
    void testValidateInvalidCapitalizedValue() {
        Annotation annotation = getFieldAnnotation(RegexTestSchema.class, "capitalizedField", Regex.class);
        assertThatThrownBy(() -> this.validator.validate("hello", "capitalizedField", annotation))
            .isInstanceOf(ValidationError.class)
            .hasMessage(
                "Value 'hello' for field 'capitalizedField' does not match the required pattern: '^[A-Z][a-z]*$'.");
    }

    @Test
    void testGetSupportedAnnotation() {
        assertThat(this.validator.getSupportedAnnotation()).isEqualTo(Regex.class);
    }
}

