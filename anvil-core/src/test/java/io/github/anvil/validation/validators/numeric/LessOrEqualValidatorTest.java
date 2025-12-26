package io.github.anvil.validation.validators.numeric;

import io.github.anvil.Schema;
import io.github.anvil.annotations.Validate;
import io.github.anvil.annotations.numeric.LessOrEqual;
import io.github.anvil.validation.ValidationError;
import org.junit.jupiter.api.Test;

import java.lang.annotation.Annotation;

import static io.github.anvil.utils.ReflectionUtils.getFieldAnnotation;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class LessOrEqualValidatorTest {
    private final LessOrEqualValidator validator = new LessOrEqualValidator();

    @Validate
    @SuppressWarnings("unused")
    static class LessOrEqualTestSchema implements Schema {
        @LessOrEqual(100.0f)
        Integer maxScore;

        @LessOrEqual(5.0f)
        Double rating;
    }

    @Test
    void testValidateValidValue() throws ValidationError {
        Annotation annotation = getFieldAnnotation(LessOrEqualTestSchema.class, "maxScore", LessOrEqual.class);
        Object returnedValue = this.validator.validate(50, "maxScore", annotation);
        assertThat(returnedValue).isNull();
    }

    @Test
    void testValidateEqualValue() throws ValidationError {
        Annotation annotation = getFieldAnnotation(LessOrEqualTestSchema.class, "maxScore", LessOrEqual.class);
        Object returnedValue = this.validator.validate(100, "maxScore", annotation);
        assertThat(returnedValue).isNull();
    }

    @Test
    void testValidateInvalidValue() {
        Annotation annotation = getFieldAnnotation(LessOrEqualTestSchema.class, "maxScore", LessOrEqual.class);
        assertThatThrownBy(() -> this.validator.validate(150, "maxScore", annotation))
            .isInstanceOf(ValidationError.class)
            .hasMessage("Field 'maxScore' must be less than or equal to the specified value (100.0).");
    }

    @Test
    void testValidateNonNumericValue() {
        Annotation annotation = getFieldAnnotation(LessOrEqualTestSchema.class, "maxScore", LessOrEqual.class);
        assertThatThrownBy(() -> this.validator.validate("not a number", "maxScore", annotation))
            .isInstanceOf(ValidationError.class)
            .hasMessage("Field 'maxScore' is not a number.");
    }

    @Test
    void testValidateValidRatingValue() throws ValidationError {
        Annotation annotation = getFieldAnnotation(LessOrEqualTestSchema.class, "rating", LessOrEqual.class);
        Object returnedValue = this.validator.validate(3.5, "rating", annotation);
        assertThat(returnedValue).isNull();
    }

    @Test
    void testValidateEqualRatingValue() throws ValidationError {
        Annotation annotation = getFieldAnnotation(LessOrEqualTestSchema.class, "rating", LessOrEqual.class);
        Object returnedValue = this.validator.validate(5.0, "rating", annotation);
        assertThat(returnedValue).isNull();
    }

    @Test
    void testValidateInvalidRatingValue() {
        Annotation annotation = getFieldAnnotation(LessOrEqualTestSchema.class, "rating", LessOrEqual.class);
        assertThatThrownBy(() -> this.validator.validate(6.0, "rating", annotation))
            .isInstanceOf(ValidationError.class)
            .hasMessage("Field 'rating' must be less than or equal to the specified value (5.0).");
    }

    @Test
    void testGetSupportedAnnotation() {
        assertThat(this.validator.getSupportedAnnotation()).isEqualTo(LessOrEqual.class);
    }
}

