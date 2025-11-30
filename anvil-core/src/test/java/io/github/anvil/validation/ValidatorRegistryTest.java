package io.github.anvil.validation;

import org.junit.jupiter.api.Test;

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
}
