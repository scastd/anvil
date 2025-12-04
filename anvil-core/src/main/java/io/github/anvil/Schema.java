package io.github.anvil;

import io.github.anvil.validation.ValidationError;

/**
 * Base interface for all Anvil schemas.
 *
 * <p>Subclasses define fields annotated with validation annotations and may implement
 * lifecycle hooks to perform custom initialization or post-processing.</p>
 */
public interface Schema {

    /**
     * Hook invoked before the schema fields are populated.
     *
     * @throws ValidationError if pre-build validation fails.
     */
    default void preBuild() throws ValidationError {
    }

    /**
     * Hook invoked after the schema fields have been populated.
     *
     * @throws ValidationError if post-build validation fails.
     */
    default void postBuild() throws ValidationError {
    }
}
