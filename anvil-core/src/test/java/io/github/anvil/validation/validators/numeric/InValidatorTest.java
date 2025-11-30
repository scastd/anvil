package io.github.anvil.validation.validators.numeric;

import io.github.anvil.Schema;
import io.github.anvil.annotations.Validate;
import io.github.anvil.annotations.numeric.In;
import io.github.anvil.validation.ValidationError;
import org.junit.jupiter.api.Test;

import java.lang.annotation.Annotation;

import static io.github.anvil.utils.ReflectionUtils.getFieldAnnotation;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

class InValidatorTest {
    private final InValidator validator = new InValidator();

    @Validate
    @SuppressWarnings("unused")
    static class InTestSchema extends Schema {
        @In({ 1, 2, 3 })
        Integer smallNumber;

        @In({ 10.5, 20.5, 30.5 })
        Double decimalNumber;
    }

    @Test
    void testValidateValidValue() throws ValidationError {
        Annotation annotation = getFieldAnnotation(InTestSchema.class, "smallNumber", In.class);
        Object returnedValue = this.validator.validate(2, "smallNumber", annotation);
        assertNull(returnedValue);
    }

    @Test
    void testValidateInvalidValue() {
        Annotation annotation = getFieldAnnotation(InTestSchema.class, "smallNumber", In.class);
        ValidationError error = assertThrows(ValidationError.class,
                                             () -> this.validator.validate(5, "smallNumber", annotation));
        assertEquals("Found value '5.0' for field 'smallNumber', but expected one of: [1.0, 2.0, 3.0].",
                     error.getMessage());
    }

    @Test
    void testValidateNonNumericValue() {
        Annotation annotation = getFieldAnnotation(InTestSchema.class, "smallNumber", In.class);
        ValidationError error = assertThrows(ValidationError.class,
                                             () -> this.validator.validate("not a number", "smallNumber", annotation));
        assertEquals("Field 'smallNumber' is not a number.", error.getMessage());
    }

    @Test
    void testValidateValidDecimalValue() throws ValidationError {
        Annotation annotation = getFieldAnnotation(InTestSchema.class, "decimalNumber", In.class);
        Object returnedValue = this.validator.validate(20.5, "decimalNumber", annotation);
        assertNull(returnedValue);
    }

    @Test
    void testValidateInvalidDecimalValue() {
        Annotation annotation = getFieldAnnotation(InTestSchema.class, "decimalNumber", In.class);
        ValidationError error = assertThrows(ValidationError.class,
                                             () -> this.validator.validate(15.5, "decimalNumber", annotation));
        assertEquals("Found value '15.5' for field 'decimalNumber', but expected one of: [10.5, 20.5, 30.5].",
                     error.getMessage());
    }

    @Test
    void testGetSupportedAnnotation() {
        assertEquals(In.class, this.validator.getSupportedAnnotation());
    }
}

