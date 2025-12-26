package io.github.anvil.validation.validators;

import io.github.anvil.Schema;
import io.github.anvil.annotations.StrEqual;
import io.github.anvil.annotations.Validate;
import io.github.anvil.validation.ValidationError;
import io.github.anvil.validation.validators.StringComparer.StringComparisonStrategy;
import org.junit.jupiter.api.Test;

import java.lang.annotation.Annotation;

import static io.github.anvil.utils.ReflectionUtils.getFieldAnnotation;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

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
        assertThat(returnedValue).isNull();
    }

    @Test
    void testValidateInvalidValueCaseSensitive() {
        Annotation annotation = getFieldAnnotation(StrEqualTestSchema.class, "caseSensitiveField", StrEqual.class);
        assertThatThrownBy(() -> this.validator.validate("First", "caseSensitiveField", annotation))
            .isInstanceOf(ValidationError.class)
            .hasMessage("Found value 'First' for field 'caseSensitiveField', but expected equal to: 'first'.");
    }

    @Test
    void testValidateValidValueCaseInsensitive() throws ValidationError {
        Annotation annotation = getFieldAnnotation(StrEqualTestSchema.class, "caseInsensitiveField", StrEqual.class);
        Object returnedValue = this.validator.validate("second", "caseInsensitiveField", annotation);
        assertThat(returnedValue).isNull();
    }

    @Test
    void testValidateValidValueCaseInsensitiveMixedCase() throws ValidationError {
        Annotation annotation = getFieldAnnotation(StrEqualTestSchema.class, "caseInsensitiveField", StrEqual.class);
        Object returnedValue = this.validator.validate("SeCOnD", "caseInsensitiveField", annotation);
        assertThat(returnedValue).isNull();
    }

    @Test
    void testValidateInvalidValueCaseInsensitive() {
        Annotation annotation = getFieldAnnotation(StrEqualTestSchema.class, "caseInsensitiveField", StrEqual.class);
        assertThatThrownBy(() -> this.validator.validate("different", "caseInsensitiveField", annotation))
            .isInstanceOf(ValidationError.class)
            .hasMessage("Found value 'different' for field 'caseInsensitiveField', but expected equal to: 'second'.");
    }

    @Test
    void testGetSupportedAnnotation() {
        assertThat(this.validator.getSupportedAnnotation()).isEqualTo(StrEqual.class);
    }
}
