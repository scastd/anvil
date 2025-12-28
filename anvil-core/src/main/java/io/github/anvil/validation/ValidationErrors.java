package io.github.anvil.validation;

import io.github.anvil.exceptions.ValidationException;

import java.util.ArrayList;
import java.util.List;

/**
 * Collects and manages {@link ValidationError} instances during schema validation.
 *
 * <p>This class is used internally by the validation processor to accumulate validation errors
 * and handle fail-fast behavior. When {@code failFast} is enabled, the first error added will
 * immediately throw a {@link ValidationException}. Otherwise, errors are collected and can be
 * checked later using {@link #throwIfAny()}.</p>
 *
 * <p>This class is not intended for direct use by application code. Applications should interact
 * with Anvil through {@link io.github.anvil.Anvil#validate(Object, Class)} and catch
 * {@link ValidationException}.</p>
 */
public class ValidationErrors {
    private final List<ValidationError> errors = new ArrayList<>();
    private final boolean failFast;

    /**
     * Creates a new instance with the specified fail-fast behavior.
     *
     * @param failFast if {@code true}, validation stops on the first error; if {@code false}, errors are accumulated.
     */
    public ValidationErrors(boolean failFast) {
        this.failFast = failFast;
    }

    /**
     * Adds a validation error to the collection.
     *
     * @param error the validation error to add.
     * @throws ValidationException if fail-fast mode is enabled.
     */
    public void addError(ValidationError error) {
        this.errors.add(error);

        if (this.failFast) {
            throw new ValidationException(this.errors);
        }
    }

    /**
     * Throws a {@link ValidationException} if any errors have been collected.
     *
     * <p>This method should be called after all validation checks have completed to ensure
     * that any accumulated errors are reported. If no errors have been collected, this method
     * returns normally.</p>
     *
     * @throws ValidationException if one or more validation errors have been collected.
     */
    public void throwIfAny() throws ValidationException {
        if (!this.errors.isEmpty()) {
            throw new ValidationException(this.errors);
        }
    }
}
