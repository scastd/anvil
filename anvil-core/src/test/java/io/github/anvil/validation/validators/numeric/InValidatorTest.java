package io.github.anvil.validation.validators.numeric;

import io.github.anvil.Schema;
import io.github.anvil.annotations.Validate;
import io.github.anvil.annotations.numeric.In;
import io.github.anvil.validation.ValidationError;
import org.junit.jupiter.api.Test;

import java.lang.annotation.Annotation;

import static io.github.anvil.utils.ReflectionUtils.getFieldAnnotation;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class InValidatorTest {
    private final InValidator validator = new InValidator();

    @Validate
    @SuppressWarnings("unused")
    static class InTestSchema implements Schema {
        @In({ 1, 2, 3 })
        Integer smallNumber;

        @In({ 10.5, 20.5, 30.5 })
        Double decimalNumber;
    }

    @Test
    void testValidateValidValue() throws ValidationError {
        Annotation annotation = getFieldAnnotation(InTestSchema.class, "smallNumber", In.class);
        Object returnedValue = this.validator.validate(2, "smallNumber", annotation);
        assertThat(returnedValue).isNull();
    }

    @Test
    void testValidateInvalidValue() {
        Annotation annotation = getFieldAnnotation(InTestSchema.class, "smallNumber", In.class);
        assertThatThrownBy(() -> this.validator.validate(5, "smallNumber", annotation))
            .isInstanceOf(ValidationError.class)
            .hasMessage("Found value '5.0' for field 'smallNumber', but expected one of: [1.0, 2.0, 3.0].");
    }

    @Test
    void testValidateNonNumericValue() {
        Annotation annotation = getFieldAnnotation(InTestSchema.class, "smallNumber", In.class);
        assertThatThrownBy(() -> this.validator.validate("not a number", "smallNumber", annotation))
            .isInstanceOf(ValidationError.class)
            .hasMessage("Field 'smallNumber' is not a number.");
    }

    @Test
    void testValidateValidDecimalValue() throws ValidationError {
        Annotation annotation = getFieldAnnotation(InTestSchema.class, "decimalNumber", In.class);
        Object returnedValue = this.validator.validate(20.5, "decimalNumber", annotation);
        assertThat(returnedValue).isNull();
    }

    @Test
    void testValidateInvalidDecimalValue() {
        Annotation annotation = getFieldAnnotation(InTestSchema.class, "decimalNumber", In.class);
        assertThatThrownBy(() -> this.validator.validate(15.5, "decimalNumber", annotation))
            .isInstanceOf(ValidationError.class)
            .hasMessage("Found value '15.5' for field 'decimalNumber', but expected one of: [10.5, 20.5, 30.5].");
    }

    @Test
    void testGetSupportedAnnotation() {
        assertThat(this.validator.getSupportedAnnotation()).isEqualTo(In.class);
    }
}

