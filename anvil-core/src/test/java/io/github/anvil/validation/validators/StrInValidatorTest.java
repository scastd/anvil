package io.github.anvil.validation.validators;

import io.github.anvil.Schema;
import io.github.anvil.annotations.StrIn;
import io.github.anvil.annotations.Validate;
import io.github.anvil.validation.ValidationError;
import io.github.anvil.validation.validators.StringComparer.StringComparisonStrategy;
import org.junit.jupiter.api.Test;

import java.lang.annotation.Annotation;

import static io.github.anvil.utils.ReflectionUtils.getFieldAnnotation;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

class StrInValidatorTest {
    private final StrInValidator validator = new StrInValidator();

    @Validate
    @SuppressWarnings("unused")
    static class StrInTestSchema extends Schema {
        @StrIn({ "apple", "banana", "orange" })
        String caseSensitiveField;

        @StrIn(value = { "Red", "Green", "Blue" }, strategy = StringComparisonStrategy.CASE_INSENSITIVE)
        String caseInsensitiveField;
    }

    @Test
    void testValidateValidValue() throws ValidationError {
        Annotation annotation = getFieldAnnotation(StrInTestSchema.class, "caseSensitiveField", StrIn.class);
        Object returnedValue = this.validator.validate("apple", "caseSensitiveField", annotation);
        assertNull(returnedValue);
    }

    @Test
    void testValidateInvalidValueCaseSensitive() {
        Annotation annotation = getFieldAnnotation(StrInTestSchema.class, "caseSensitiveField", StrIn.class);
        ValidationError error = assertThrows(ValidationError.class,
                                             () -> this.validator.validate("Apple", "caseSensitiveField", annotation));
        assertEquals("Field 'caseSensitiveField' with value 'Apple' is not in the allowed set: [apple, banana, orange]",
                     error.getMessage());
    }

    @Test
    void testValidateValidValueCaseInsensitive() throws ValidationError {
        Annotation annotation = getFieldAnnotation(StrInTestSchema.class, "caseInsensitiveField", StrIn.class);
        Object returnedValue = this.validator.validate("red", "caseInsensitiveField", annotation);
        assertNull(returnedValue);
    }

    @Test
    void testValidateValidValueCaseInsensitiveMixedCase() throws ValidationError {
        Annotation annotation = getFieldAnnotation(StrInTestSchema.class, "caseInsensitiveField", StrIn.class);
        Object returnedValue = this.validator.validate("GreeN", "caseInsensitiveField", annotation);
        assertNull(returnedValue);
    }

    @Test
    void testValidateInvalidValueCaseInsensitive() {
        Annotation annotation = getFieldAnnotation(StrInTestSchema.class, "caseInsensitiveField", StrIn.class);
        ValidationError error = assertThrows(ValidationError.class,
                                             () -> this.validator.validate("Yellow", "caseInsensitiveField",
                                                                           annotation));
        assertEquals("Field 'caseInsensitiveField' with value 'Yellow' is not in the allowed set: [Red, Green, Blue]",
                     error.getMessage());
    }

    @Test
    void testGetSupportedAnnotation() {
        assertEquals(StrIn.class, this.validator.getSupportedAnnotation());
    }
}
