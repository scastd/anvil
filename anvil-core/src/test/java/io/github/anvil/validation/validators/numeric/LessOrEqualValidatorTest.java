package io.github.anvil.validation.validators.numeric;

import io.github.anvil.Schema;
import io.github.anvil.annotations.Validate;
import io.github.anvil.annotations.numeric.LessOrEqual;
import io.github.anvil.validation.ValidationError;
import org.junit.jupiter.api.Test;

import java.lang.annotation.Annotation;

import static io.github.anvil.utils.ReflectionUtils.getFieldAnnotation;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

class LessOrEqualValidatorTest {
    private final LessOrEqualValidator validator = new LessOrEqualValidator();

    @Validate
    @SuppressWarnings("unused")
    static class LessOrEqualTestSchema extends Schema {
        @LessOrEqual(100.0f)
        Integer maxScore;

        @LessOrEqual(5.0f)
        Double rating;
    }

    @Test
    void testValidateValidValue() throws ValidationError {
        Annotation annotation = getFieldAnnotation(LessOrEqualTestSchema.class, "maxScore", LessOrEqual.class);
        Object returnedValue = this.validator.validate(50, "maxScore", annotation);
        assertNull(returnedValue);
    }

    @Test
    void testValidateEqualValue() throws ValidationError {
        Annotation annotation = getFieldAnnotation(LessOrEqualTestSchema.class, "maxScore", LessOrEqual.class);
        Object returnedValue = this.validator.validate(100, "maxScore", annotation);
        assertNull(returnedValue);
    }

    @Test
    void testValidateInvalidValue() {
        Annotation annotation = getFieldAnnotation(LessOrEqualTestSchema.class, "maxScore", LessOrEqual.class);
        ValidationError error = assertThrows(ValidationError.class,
                                             () -> this.validator.validate(150, "maxScore", annotation));
        assertEquals("Field 'maxScore' must be less than or equal to the specified value (100.0).",
                     error.getMessage());
    }

    @Test
    void testValidateNonNumericValue() {
        Annotation annotation = getFieldAnnotation(LessOrEqualTestSchema.class, "maxScore", LessOrEqual.class);
        ValidationError error = assertThrows(ValidationError.class,
                                             () -> this.validator.validate("not a number", "maxScore", annotation));
        assertEquals("Field 'maxScore' is not a number.", error.getMessage());
    }

    @Test
    void testValidateValidRatingValue() throws ValidationError {
        Annotation annotation = getFieldAnnotation(LessOrEqualTestSchema.class, "rating", LessOrEqual.class);
        Object returnedValue = this.validator.validate(3.5, "rating", annotation);
        assertNull(returnedValue);
    }

    @Test
    void testValidateEqualRatingValue() throws ValidationError {
        Annotation annotation = getFieldAnnotation(LessOrEqualTestSchema.class, "rating", LessOrEqual.class);
        Object returnedValue = this.validator.validate(5.0, "rating", annotation);
        assertNull(returnedValue);
    }

    @Test
    void testValidateInvalidRatingValue() {
        Annotation annotation = getFieldAnnotation(LessOrEqualTestSchema.class, "rating", LessOrEqual.class);
        ValidationError error = assertThrows(ValidationError.class,
                                             () -> this.validator.validate(6.0, "rating", annotation));
        assertEquals("Field 'rating' must be less than or equal to the specified value (5.0).",
                     error.getMessage());
    }

    @Test
    void testGetSupportedAnnotation() {
        assertEquals(LessOrEqual.class, this.validator.getSupportedAnnotation());
    }
}

