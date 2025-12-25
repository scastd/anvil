package io.github.anvil.exceptions;

/**
 * Thrown when an instance of a schema class cannot be created.
 */
public class NonConstructibleException extends RuntimeException {

    /**
     * Creates a new exception indicating that the given class could not be instantiated.
     *
     * @param clazz the class that failed to instantiate.
     * @param cause the underlying cause of the instantiation failure.
     */
    public NonConstructibleException(Class<?> clazz, Throwable cause) {
        super(
            "Failed to create an instance of class: %s. Ensure it has a public no-arg/all-args constructor."
                .formatted(clazz.getName()),
            cause
        );
    }
}
