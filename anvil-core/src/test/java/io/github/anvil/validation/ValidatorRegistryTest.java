package io.github.anvil.validation;

import org.junit.jupiter.api.Test;

import java.lang.annotation.Annotation;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ValidatorRegistryTest {
    private final ValidatorRegistry validatorRegistry = ValidatorRegistry.getInstance();

    @Test
    void testGetValidatorThrowsWhenNotFound() {
        assertThatThrownBy(() -> validatorRegistry.getValidator(Deprecated.class))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("No validator found for annotation: java.lang.Deprecated");
    }

    @Test
    void testAddValidator() {
        Validator validator = new Validator() {
            @Override
            public Object validate(Object value, String fieldName, Annotation annotation) {
                return null;
            }

            @Override
            public Class<? extends Annotation> getSupportedAnnotation() {
                return Deprecated.class;
            }
        };

        validatorRegistry.addValidator(validator);
        Validator retrievedValidator = validatorRegistry.getValidator(Deprecated.class);

        assertThat(retrievedValidator).isEqualTo(validator);
    }
}
