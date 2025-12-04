package io.github.anvil.validation.validators;

import io.github.anvil.Schema;
import io.github.anvil.annotations.StrEqual;
import io.github.anvil.annotations.Validate;
import io.github.anvil.validation.ValidationError;
import io.github.anvil.validation.validators.StringComparer.StringComparisonStrategy;
import org.junit.jupiter.api.Test;

import java.lang.annotation.Annotation;

import static io.github.anvil.utils.ReflectionUtils.getFieldAnnotation;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

class StrEqualValidatorTest {
    private final StrEqualValidator validator = new StrEqualValidator();

    @Validate
    @SuppressWarnings("unused")
    static class StrEqualTestSchema implements Schema {
        @StrEqual("first")
        String caseSensitiveField;

        @StrEqual(value = "second", strategy = StringComparisonStrategy.CASE_INSENSITIVE)
        String caseInsensitiveField;
    }

    @Test
    void testValidateValidValue() throws ValidationError {
        Annotation annotation = getFieldAnnotation(StrEqualTestSchema.class, "caseSensitiveField", StrEqual.class);
        Object returnedValue = this.validator.validate("first", "caseSensitiveField", annotation);
        assertNull(returnedValue);
    }

    @Test
    void testValidateInvalidValueCaseSensitive() {
        Annotation annotation = getFieldAnnotation(StrEqualTestSchema.class, "caseSensitiveField", StrEqual.class);
        ValidationError error = assertThrows(ValidationError.class,
                                             () -> this.validator.validate("First", "caseSensitiveField",
                                                                           annotation));
        assertEquals("Found value 'First' for field 'caseSensitiveField', but expected equal to: 'first'.",
                     error.getMessage());
    }

    @Test
    void testValidateValidValueCaseInsensitive() throws ValidationError {
        Annotation annotation = getFieldAnnotation(StrEqualTestSchema.class, "caseInsensitiveField", StrEqual.class);
        Object returnedValue = this.validator.validate("second", "caseInsensitiveField", annotation);
        assertNull(returnedValue);
    }

    @Test
    void testValidateValidValueCaseInsensitiveMixedCase() throws ValidationError {
        Annotation annotation = getFieldAnnotation(StrEqualTestSchema.class, "caseInsensitiveField", StrEqual.class);
        Object returnedValue = this.validator.validate("SeCOnD", "caseInsensitiveField", annotation);
        assertNull(returnedValue);
    }

    @Test
    void testValidateInvalidValueCaseInsensitive() {
        Annotation annotation = getFieldAnnotation(StrEqualTestSchema.class, "caseInsensitiveField", StrEqual.class);
        ValidationError error = assertThrows(ValidationError.class,
                                             () -> this.validator.validate("different", "caseInsensitiveField",
                                                                           annotation));
        assertEquals("Found value 'different' for field 'caseInsensitiveField', but expected equal to: 'second'.",
                     error.getMessage());
    }

    @Test
    void testGetSupportedAnnotation() {
        assertEquals(StrEqual.class, this.validator.getSupportedAnnotation());
    }
}
