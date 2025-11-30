package io.github.anvil.validation.validators.numeric;

import io.github.anvil.Schema;
import io.github.anvil.annotations.Validate;
import io.github.anvil.annotations.numeric.Between;
import io.github.anvil.validation.ValidationError;
import org.junit.jupiter.api.Test;

import java.lang.annotation.Annotation;

import static io.github.anvil.utils.ReflectionUtils.getFieldAnnotation;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

class BetweenValidatorTest {
    private final BetweenValidator validator = new BetweenValidator();

    @Validate
    @SuppressWarnings("unused")
    static class BetweenTestSchema extends Schema {
        @Between(min = 0.0f, max = 100.0f)
        Integer percentage;

        @Between(min = -10.0f, max = 10.0f)
        Double temperature;
    }

    @Test
    void testValidateValidValue() throws ValidationError {
        Annotation annotation = getFieldAnnotation(BetweenTestSchema.class, "percentage", Between.class);
        Object returnedValue = this.validator.validate(50, "percentage", annotation);
        assertNull(returnedValue);
    }

    @Test
    void testValidateMinBoundaryInclusive() throws ValidationError {
        Annotation annotation = getFieldAnnotation(BetweenTestSchema.class, "percentage", Between.class);
        Object returnedValue = this.validator.validate(0, "percentage", annotation);
        assertNull(returnedValue);
    }

    @Test
    void testValidateMaxBoundaryExclusive() {
        Annotation annotation = getFieldAnnotation(BetweenTestSchema.class, "percentage", Between.class);
        ValidationError error = assertThrows(ValidationError.class,
                                             () -> this.validator.validate(100, "percentage", annotation));
        assertEquals("Field 'percentage' must be between 0.0 and 100.0 (not inclusive), but found 100.0",
                     error.getMessage());
    }

    @Test
    void testValidateBelowMin() {
        Annotation annotation = getFieldAnnotation(BetweenTestSchema.class, "percentage", Between.class);
        ValidationError error = assertThrows(ValidationError.class,
                                             () -> this.validator.validate(-1, "percentage", annotation));
        assertEquals("Field 'percentage' must be between 0.0 and 100.0 (not inclusive), but found -1.0",
                     error.getMessage());
    }

    @Test
    void testValidateAboveMax() {
        Annotation annotation = getFieldAnnotation(BetweenTestSchema.class, "percentage", Between.class);
        ValidationError error = assertThrows(ValidationError.class,
                                             () -> this.validator.validate(150, "percentage", annotation));
        assertEquals("Field 'percentage' must be between 0.0 and 100.0 (not inclusive), but found 150.0",
                     error.getMessage());
    }

    @Test
    void testValidateNonNumericValue() {
        Annotation annotation = getFieldAnnotation(BetweenTestSchema.class, "percentage", Between.class);
        ValidationError error = assertThrows(ValidationError.class,
                                             () -> this.validator.validate("not a number", "percentage", annotation));
        assertEquals("Field 'percentage' is not a number.", error.getMessage());
    }

    @Test
    void testValidateValidDoubleValue() throws ValidationError {
        Annotation annotation = getFieldAnnotation(BetweenTestSchema.class, "temperature", Between.class);
        Object returnedValue = this.validator.validate(5.5, "temperature", annotation);
        assertNull(returnedValue);
    }

    @Test
    void testValidateNegativeValue() throws ValidationError {
        Annotation annotation = getFieldAnnotation(BetweenTestSchema.class, "temperature", Between.class);
        Object returnedValue = this.validator.validate(-5.0, "temperature", annotation);
        assertNull(returnedValue);
    }

    @Test
    void testGetSupportedAnnotation() {
        assertEquals(Between.class, this.validator.getSupportedAnnotation());
    }
}

