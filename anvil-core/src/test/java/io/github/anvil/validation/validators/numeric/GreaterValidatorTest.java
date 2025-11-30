package io.github.anvil.validation.validators.numeric;

import io.github.anvil.Schema;
import io.github.anvil.annotations.Validate;
import io.github.anvil.annotations.numeric.Greater;
import io.github.anvil.validation.ValidationError;
import org.junit.jupiter.api.Test;

import java.lang.annotation.Annotation;

import static io.github.anvil.utils.ReflectionUtils.getFieldAnnotation;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

class GreaterValidatorTest {
    private final GreaterValidator validator = new GreaterValidator();

    @Validate
    @SuppressWarnings("unused")
    static class GreaterTestSchema extends Schema {
        @Greater(0.0f)
        Integer positive;

        @Greater(18.0f)
        Integer adult;
    }

    @Test
    void testValidateValidValue() throws ValidationError {
        Annotation annotation = getFieldAnnotation(GreaterTestSchema.class, "positive", Greater.class);
        Object returnedValue = this.validator.validate(5, "positive", annotation);
        assertNull(returnedValue);
    }

    @Test
    void testValidateInvalidEqualValue() {
        Annotation annotation = getFieldAnnotation(GreaterTestSchema.class, "positive", Greater.class);
        ValidationError error = assertThrows(ValidationError.class,
                                             () -> this.validator.validate(0, "positive", annotation));
        assertEquals("Field 'positive' must be greater than the specified value (0.0).",
                     error.getMessage());
    }

    @Test
    void testValidateInvalidValue() {
        Annotation annotation = getFieldAnnotation(GreaterTestSchema.class, "positive", Greater.class);
        ValidationError error = assertThrows(ValidationError.class,
                                             () -> this.validator.validate(-1, "positive", annotation));
        assertEquals("Field 'positive' must be greater than the specified value (0.0).",
                     error.getMessage());
    }

    @Test
    void testValidateNonNumericValue() {
        Annotation annotation = getFieldAnnotation(GreaterTestSchema.class, "positive", Greater.class);
        ValidationError error = assertThrows(ValidationError.class,
                                             () -> this.validator.validate("not a number", "positive", annotation));
        assertEquals("Field 'positive' is not a number.", error.getMessage());
    }

    @Test
    void testValidateValidAdultValue() throws ValidationError {
        Annotation annotation = getFieldAnnotation(GreaterTestSchema.class, "adult", Greater.class);
        Object returnedValue = this.validator.validate(25, "adult", annotation);
        assertNull(returnedValue);
    }

    @Test
    void testValidateInvalidEqualAdultValue() {
        Annotation annotation = getFieldAnnotation(GreaterTestSchema.class, "adult", Greater.class);
        ValidationError error = assertThrows(ValidationError.class,
                                             () -> this.validator.validate(18, "adult", annotation));
        assertEquals("Field 'adult' must be greater than the specified value (18.0).",
                     error.getMessage());
    }

    @Test
    void testGetSupportedAnnotation() {
        assertEquals(Greater.class, this.validator.getSupportedAnnotation());
    }
}

