package io.github.anvil;

import io.github.anvil.validation.ValidationError;

/**
 * Base interface for all Anvil schemas.
 *
 * <p>Subclasses define fields annotated with validation annotations and may implement
 * lifecycle hooks to perform custom post-processing.</p>
 */
public interface Schema {

    /**
     * Hook invoked after the schema fields have been populated.
     *
     * @throws ValidationError if post-build validation fails.
     */
    default void postBuild() throws ValidationError {
    }
}
