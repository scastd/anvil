package io.github.anvil.validation.validators.numeric;

import io.github.anvil.Schema;
import io.github.anvil.annotations.Validate;
import io.github.anvil.annotations.numeric.Equal;
import io.github.anvil.validation.ValidationError;
import org.junit.jupiter.api.Test;

import java.lang.annotation.Annotation;

import static io.github.anvil.utils.ReflectionUtils.getFieldAnnotation;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

class EqualValidatorTest {
    private final EqualValidator validator = new EqualValidator();

    @Validate
    @SuppressWarnings("unused")
    static class EqualTestSchema extends Schema {
        @Equal(42.0)
        Integer integerField;

        @Equal(3.14)
        Double doubleField;
    }

    @Test
    void testValidateValidValue() throws ValidationError {
        Annotation annotation = getFieldAnnotation(EqualTestSchema.class, "integerField", Equal.class);
        Object returnedValue = this.validator.validate(42, "integerField", annotation);
        assertNull(returnedValue);
    }

    @Test
    void testValidateInvalidValueGreaterThan() {
        Annotation annotation = getFieldAnnotation(EqualTestSchema.class, "integerField", Equal.class);
        ValidationError error = assertThrows(ValidationError.class,
                                             () -> this.validator.validate(43, "integerField", annotation));
        assertEquals("Field 'integerField' must be equal to the specified value (42.0).",
                     error.getMessage());
    }

    @Test
    void testValidateInvalidValueLessThan() {
        Annotation annotation = getFieldAnnotation(EqualTestSchema.class, "integerField", Equal.class);
        ValidationError error = assertThrows(ValidationError.class,
                                             () -> this.validator.validate(41, "integerField", annotation));
        assertEquals("Field 'integerField' must be equal to the specified value (42.0).",
                     error.getMessage());
    }

    @Test
    void testValidateNonNumericValue() {
        Annotation annotation = getFieldAnnotation(EqualTestSchema.class, "integerField", Equal.class);
        ValidationError error = assertThrows(ValidationError.class,
                                             () -> this.validator.validate("not a number", "integerField", annotation));
        assertEquals("Field 'integerField' is not a number.", error.getMessage());
    }

    @Test
    void testValidateValidDoubleValue() throws ValidationError {
        Annotation annotation = getFieldAnnotation(EqualTestSchema.class, "doubleField", Equal.class);
        Object returnedValue = this.validator.validate(3.14, "doubleField", annotation);
        assertNull(returnedValue);
    }

    @Test
    void testValidateInvalidDoubleValue() {
        Annotation annotation = getFieldAnnotation(EqualTestSchema.class, "doubleField", Equal.class);
        ValidationError error = assertThrows(ValidationError.class,
                                             () -> this.validator.validate(3.15, "doubleField", annotation));
        assertEquals("Field 'doubleField' must be equal to the specified value (3.14).",
                     error.getMessage());
    }

    @Test
    void testGetSupportedAnnotation() {
        assertEquals(Equal.class, this.validator.getSupportedAnnotation());
    }
}

