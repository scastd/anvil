package io.github.anvil.validation;

import org.junit.jupiter.api.Test;

import java.lang.annotation.Annotation;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ValidatorRegistryTest {
    private final ValidatorRegistry validatorRegistry = ValidatorRegistry.getInstance();

    @Test
    void testGetValidatorThrowsWhenNotFound() {
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> validatorRegistry.getValidator(Deprecated.class)
        );

        assertEquals("No validator found for annotation: java.lang.Deprecated", exception.getMessage());
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

        assertEquals(validator, retrievedValidator);
    }
}
