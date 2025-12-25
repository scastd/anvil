package io.github.anvil.validation.validators;

import io.github.anvil.Schema;
import io.github.anvil.annotations.OptionalValue;
import io.github.anvil.annotations.Validate;
import io.github.anvil.validation.ValidationError;
import org.junit.jupiter.api.Test;

import java.lang.annotation.Annotation;

import static io.github.anvil.utils.ReflectionUtils.getFieldAnnotation;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class OptionalValueValidatorTest {
    private final OptionalValueValidator validator = new OptionalValueValidator();

    @Validate
    @SuppressWarnings("unused")
    static class OptionalValueTestSchema implements Schema {
        @OptionalValue
        String optionalField;
    }

    @Test
    void testValidateNullValue() throws ValidationError {
        Annotation annotation = getFieldAnnotation(OptionalValueTestSchema.class, "optionalField", OptionalValue.class);
        Object returnedValue = this.validator.validate(null, "optionalField", annotation);
        assertNull(returnedValue);
    }

    @Test
    void testValidateNonNullValue() throws ValidationError {
        Annotation annotation = getFieldAnnotation(OptionalValueTestSchema.class, "optionalField", OptionalValue.class);
        Object returnedValue = this.validator.validate("value", "optionalField", annotation);
        assertEquals("value", returnedValue);
    }

    @Test
    void testGetSupportedAnnotation() {
        assertEquals(OptionalValue.class, this.validator.getSupportedAnnotation());
    }
}
