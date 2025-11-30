package io.github.anvil.validation.validators.numeric;

import io.github.anvil.Schema;
import io.github.anvil.annotations.Validate;
import io.github.anvil.annotations.numeric.GreaterOrEqual;
import io.github.anvil.validation.ValidationError;
import org.junit.jupiter.api.Test;

import java.lang.annotation.Annotation;

import static io.github.anvil.utils.ReflectionUtils.getFieldAnnotation;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

class GreaterOrEqualValidatorTest {
    private final GreaterOrEqualValidator validator = new GreaterOrEqualValidator();

    @Validate
    @SuppressWarnings("unused")
    static class GreaterOrEqualTestSchema extends Schema {
        @GreaterOrEqual(0.0f)
        Integer nonNegative;

        @GreaterOrEqual(18.0f)
        Integer age;
    }

    @Test
    void testValidateValidValue() throws ValidationError {
        Annotation annotation = getFieldAnnotation(GreaterOrEqualTestSchema.class, "nonNegative", GreaterOrEqual.class);
        Object returnedValue = this.validator.validate(5, "nonNegative", annotation);
        assertNull(returnedValue);
    }

    @Test
    void testValidateEqualValue() throws ValidationError {
        Annotation annotation = getFieldAnnotation(GreaterOrEqualTestSchema.class, "nonNegative", GreaterOrEqual.class);
        Object returnedValue = this.validator.validate(0, "nonNegative", annotation);
        assertNull(returnedValue);
    }

    @Test
    void testValidateInvalidValue() {
        Annotation annotation = getFieldAnnotation(GreaterOrEqualTestSchema.class, "nonNegative", GreaterOrEqual.class);
        ValidationError error = assertThrows(ValidationError.class,
                                             () -> this.validator.validate(-1, "nonNegative", annotation));
        assertEquals("Field 'nonNegative' must be greater than or equal to the specified value (0.0).",
                     error.getMessage());
    }

    @Test
    void testValidateNonNumericValue() {
        Annotation annotation = getFieldAnnotation(GreaterOrEqualTestSchema.class, "nonNegative", GreaterOrEqual.class);
        ValidationError error = assertThrows(ValidationError.class,
                                             () -> this.validator.validate("not a number", "nonNegative", annotation));
        assertEquals("Field 'nonNegative' is not a number.", error.getMessage());
    }

    @Test
    void testValidateValidAgeValue() throws ValidationError {
        Annotation annotation = getFieldAnnotation(GreaterOrEqualTestSchema.class, "age", GreaterOrEqual.class);
        Object returnedValue = this.validator.validate(25, "age", annotation);
        assertNull(returnedValue);
    }

    @Test
    void testValidateEqualAgeValue() throws ValidationError {
        Annotation annotation = getFieldAnnotation(GreaterOrEqualTestSchema.class, "age", GreaterOrEqual.class);
        Object returnedValue = this.validator.validate(18, "age", annotation);
        assertNull(returnedValue);
    }

    @Test
    void testValidateInvalidAgeValue() {
        Annotation annotation = getFieldAnnotation(GreaterOrEqualTestSchema.class, "age", GreaterOrEqual.class);
        ValidationError error = assertThrows(ValidationError.class,
                                             () -> this.validator.validate(17, "age", annotation));
        assertEquals("Field 'age' must be greater than or equal to the specified value (18.0).",
                     error.getMessage());
    }

    @Test
    void testGetSupportedAnnotation() {
        assertEquals(GreaterOrEqual.class, this.validator.getSupportedAnnotation());
    }
}

