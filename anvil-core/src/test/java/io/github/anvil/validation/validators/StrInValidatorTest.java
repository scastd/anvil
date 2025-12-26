package io.github.anvil.validation.validators;

import io.github.anvil.Schema;
import io.github.anvil.annotations.StrIn;
import io.github.anvil.annotations.Validate;
import io.github.anvil.validation.ValidationError;
import io.github.anvil.validation.validators.StringComparer.StringComparisonStrategy;
import org.junit.jupiter.api.Test;

import java.lang.annotation.Annotation;

import static io.github.anvil.utils.ReflectionUtils.getFieldAnnotation;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class StrInValidatorTest {
    private final StrInValidator validator = new StrInValidator();

    @Validate
    @SuppressWarnings("unused")
    static class StrInTestSchema implements Schema {
        @StrIn({ "apple", "banana", "orange" })
        String caseSensitiveField;

        @StrIn(value = { "Red", "Green", "Blue" }, strategy = StringComparisonStrategy.CASE_INSENSITIVE)
        String caseInsensitiveField;
    }

    @Test
    void testValidateValidValue() throws ValidationError {
        Annotation annotation = getFieldAnnotation(StrInTestSchema.class, "caseSensitiveField", StrIn.class);
        Object returnedValue = this.validator.validate("apple", "caseSensitiveField", annotation);
        assertThat(returnedValue).isNull();
    }

    @Test
    void testValidateInvalidValueCaseSensitive() {
        Annotation annotation = getFieldAnnotation(StrInTestSchema.class, "caseSensitiveField", StrIn.class);
        assertThatThrownBy(() -> this.validator.validate("Apple", "caseSensitiveField", annotation))
            .isInstanceOf(ValidationError.class)
            .hasMessage(
                "Field 'caseSensitiveField' with value 'Apple' is not in the allowed set: [apple, banana, orange]");
    }

    @Test
    void testValidateValidValueCaseInsensitive() throws ValidationError {
        Annotation annotation = getFieldAnnotation(StrInTestSchema.class, "caseInsensitiveField", StrIn.class);
        Object returnedValue = this.validator.validate("red", "caseInsensitiveField", annotation);
        assertThat(returnedValue).isNull();
    }

    @Test
    void testValidateValidValueCaseInsensitiveMixedCase() throws ValidationError {
        Annotation annotation = getFieldAnnotation(StrInTestSchema.class, "caseInsensitiveField", StrIn.class);
        Object returnedValue = this.validator.validate("GreeN", "caseInsensitiveField", annotation);
        assertThat(returnedValue).isNull();
    }

    @Test
    void testValidateInvalidValueCaseInsensitive() {
        Annotation annotation = getFieldAnnotation(StrInTestSchema.class, "caseInsensitiveField", StrIn.class);
        assertThatThrownBy(() -> this.validator.validate("Yellow", "caseInsensitiveField", annotation))
            .isInstanceOf(ValidationError.class)
            .hasMessage(
                "Field 'caseInsensitiveField' with value 'Yellow' is not in the allowed set: [Red, Green, Blue]");
    }

    @Test
    void testGetSupportedAnnotation() {
        assertThat(this.validator.getSupportedAnnotation()).isEqualTo(StrIn.class);
    }
}
