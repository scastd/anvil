package io.github.anvil.validation.validators.numeric;

import io.github.anvil.Schema;
import io.github.anvil.annotations.Validate;
import io.github.anvil.annotations.numeric.Greater;
import io.github.anvil.validation.ValidationError;
import org.junit.jupiter.api.Test;

import java.lang.annotation.Annotation;

import static io.github.anvil.utils.ReflectionUtils.getFieldAnnotation;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class GreaterValidatorTest {
    private final GreaterValidator validator = new GreaterValidator();

    @Validate
    @SuppressWarnings("unused")
    static class GreaterTestSchema implements Schema {
        @Greater(0.0f)
        Integer positive;

        @Greater(18.0f)
        Integer adult;
    }

    @Test
    void testValidateValidValue() throws ValidationError {
        Annotation annotation = getFieldAnnotation(GreaterTestSchema.class, "positive", Greater.class);
        Object returnedValue = this.validator.validate(5, "positive", annotation);
        assertThat(returnedValue).isNull();
    }

    @Test
    void testValidateInvalidEqualValue() {
        Annotation annotation = getFieldAnnotation(GreaterTestSchema.class, "positive", Greater.class);
        assertThatThrownBy(() -> this.validator.validate(0, "positive", annotation))
            .isInstanceOf(ValidationError.class)
            .hasMessage("Field 'positive' must be greater than the specified value (0.0).");
    }

    @Test
    void testValidateInvalidValue() {
        Annotation annotation = getFieldAnnotation(GreaterTestSchema.class, "positive", Greater.class);
        assertThatThrownBy(() -> this.validator.validate(-1, "positive", annotation))
            .isInstanceOf(ValidationError.class)
            .hasMessage("Field 'positive' must be greater than the specified value (0.0).");
    }

    @Test
    void testValidateNonNumericValue() {
        Annotation annotation = getFieldAnnotation(GreaterTestSchema.class, "positive", Greater.class);
        assertThatThrownBy(() -> this.validator.validate("not a number", "positive", annotation))
            .isInstanceOf(ValidationError.class)
            .hasMessage("Field 'positive' is not a number.");
    }

    @Test
    void testValidateValidAdultValue() throws ValidationError {
        Annotation annotation = getFieldAnnotation(GreaterTestSchema.class, "adult", Greater.class);
        Object returnedValue = this.validator.validate(25, "adult", annotation);
        assertThat(returnedValue).isNull();
    }

    @Test
    void testValidateInvalidEqualAdultValue() {
        Annotation annotation = getFieldAnnotation(GreaterTestSchema.class, "adult", Greater.class);
        assertThatThrownBy(() -> this.validator.validate(18, "adult", annotation))
            .isInstanceOf(ValidationError.class)
            .hasMessage("Field 'adult' must be greater than the specified value (18.0).");
    }

    @Test
    void testGetSupportedAnnotation() {
        assertThat(this.validator.getSupportedAnnotation()).isEqualTo(Greater.class);
    }
}

