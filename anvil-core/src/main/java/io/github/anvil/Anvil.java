package io.github.anvil;

import io.github.anvil.exceptions.ValidationException;
import io.github.anvil.processor.Processor;
import io.github.anvil.validation.ValidationError;

import java.util.ArrayList;
import java.util.List;

/**
 * Entry point for validating inputs and building {@link Schema} instances.
 *
 * <p>An {@code Anvil} instance delegates to a {@link Processor} to read values from the
 * input type {@code IN}, apply all validation rules, and construct the target schema.</p>
 *
 * @param <IN> the input type from which field values are read.
 */
public class Anvil<IN> {
    private final Processor<IN> processor;
    private final List<ValidationError> validationErrorList = new ArrayList<>();

    /**
     * Creates a new {@code Anvil} instance using the given processor.
     *
     * @param processor the processor responsible for extracting and validating input values.
     */
    public Anvil(Processor<IN> processor) {
        this.processor = processor;
    }

    /**
     * Validates the given input and builds a schema instance of the requested type.
     *
     * <p>If validation fails, a {@link ValidationException} is thrown containing all collected
     * {@link ValidationError}s.</p>
     *
     * @param in    the input to validate.
     * @param clazz the schema class to instantiate.
     * @param <S>   the schema subtype to be created.
     * @return the validated schema instance.
     * @throws ValidationException if one or more validation errors occur.
     */
    public final <S extends Schema> S validate(IN in, Class<S> clazz) {
        S validatedInstance = null;

        try {
            validatedInstance = this.processor.process(in, clazz, this.validationErrorList);
        } catch (ValidationError e) {
            this.validationErrorList.add(e);
        }

        if (!this.validationErrorList.isEmpty()) {
            throw new ValidationException(this.validationErrorList);
        }

        return validatedInstance;
    }
}
