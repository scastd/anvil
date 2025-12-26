package io.github.anvil.validation.validators;

import io.github.anvil.Schema;
import io.github.anvil.annotations.EnumValue;
import io.github.anvil.annotations.Validate;
import io.github.anvil.validation.ValidationError;
import org.junit.jupiter.api.Test;

import java.lang.annotation.Annotation;

import static io.github.anvil.utils.ReflectionUtils.getFieldAnnotation;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

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
        assertThat(returnedValue).isEqualTo(Color.RED);
    }

    @Test
    void testValidateInvalidEnumValue() {
        Annotation annotation = getFieldAnnotation(EnumValueTestSchema.class, "color", EnumValue.class);
        assertThatThrownBy(() -> this.validator.validate("YELLOW", "color", annotation))
            .isInstanceOf(ValidationError.class)
            .hasMessage(
                "Field 'color' has value 'YELLOW' which is not among the allowed enum values for the enum 'Color': [RED, GREEN, BLUE]");
    }

    @Test
    void testValidateInvalidEnumValueCaseSensitive() {
        Annotation annotation = getFieldAnnotation(EnumValueTestSchema.class, "color", EnumValue.class);
        assertThatThrownBy(() -> this.validator.validate("red", "color", annotation))
            .isInstanceOf(ValidationError.class)
            .hasMessage(
                "Field 'color' has value 'red' which is not among the allowed enum values for the enum 'Color': [RED, GREEN, BLUE]");
    }

    @Test
    void testValidateNonStringValue() {
        Annotation annotation = getFieldAnnotation(EnumValueTestSchema.class, "color", EnumValue.class);
        assertThatThrownBy(() -> this.validator.validate(123, "color", annotation))
            .isInstanceOf(ValidationError.class)
            .hasMessage("Field 'color' is not a string (123), cannot validate enum value.");
    }

    @Test
    void testGetSupportedAnnotation() {
        assertThat(this.validator.getSupportedAnnotation()).isEqualTo(EnumValue.class);
    }
}

