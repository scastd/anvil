package io.github.anvil.validation.validators.numeric;

import io.github.anvil.Schema;
import io.github.anvil.annotations.Validate;
import io.github.anvil.annotations.numeric.Less;
import io.github.anvil.validation.ValidationError;
import org.junit.jupiter.api.Test;

import java.lang.annotation.Annotation;

import static io.github.anvil.utils.ReflectionUtils.getFieldAnnotation;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

class LessValidatorTest {
    private final LessValidator validator = new LessValidator();

    @Validate
    @SuppressWarnings("unused")
    static class LessTestSchema extends Schema {
        @Less(100.0f)
        Integer score;

        @Less(10.0f)
        Double value;
    }

    @Test
    void testValidateValidValue() throws ValidationError {
        Annotation annotation = getFieldAnnotation(LessTestSchema.class, "score", Less.class);
        Object returnedValue = this.validator.validate(50, "score", annotation);
        assertNull(returnedValue);
    }

    @Test
    void testValidateInvalidEqualValue() {
        Annotation annotation = getFieldAnnotation(LessTestSchema.class, "score", Less.class);
        ValidationError error = assertThrows(ValidationError.class,
                                             () -> this.validator.validate(100, "score", annotation));
        assertEquals("Field 'score' must be less than the specified value (100.0).",
                     error.getMessage());
    }

    @Test
    void testValidateNonNumericValue() {
        Annotation annotation = getFieldAnnotation(LessTestSchema.class, "score", Less.class);
        ValidationError error = assertThrows(ValidationError.class,
                                             () -> this.validator.validate("not a number", "score", annotation));
        assertEquals("Field 'score' is not a number.", error.getMessage());
    }

    @Test
    void testValidateValidDoubleValue() throws ValidationError {
        Annotation annotation = getFieldAnnotation(LessTestSchema.class, "value", Less.class);
        Object returnedValue = this.validator.validate(5.0, "value", annotation);
        assertNull(returnedValue);
    }

    @Test
    void testValidateInvalidEqualDoubleValue() {
        Annotation annotation = getFieldAnnotation(LessTestSchema.class, "value", Less.class);
        ValidationError error = assertThrows(ValidationError.class,
                                             () -> this.validator.validate(10.0, "value", annotation));
        assertEquals("Field 'value' must be less than the specified value (10.0).",
                     error.getMessage());
    }

    @Test
    void testGetSupportedAnnotation() {
        assertEquals(Less.class, this.validator.getSupportedAnnotation());
    }
}

