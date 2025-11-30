package io.github.anvil.validation.validators;

import io.github.anvil.annotations.ValidateField;
import io.github.anvil.validation.ValidationError;
import io.github.anvil.validation.Validator;

import java.lang.annotation.Annotation;

/**
 * Validator for the {@link ValidateField} annotation, enforcing required/optional semantics.
 */
public class ValidateFieldValidator implements Validator {

    /**
     * Validates that a required field is present, or passes through the value when optional.
     *
     * @param value      the value to validate.
     * @param fieldName  the name of the field being validated.
     * @param annotation the {@link ValidateField} annotation instance.
     * @return the original value when validation succeeds.
     * @throws ValidationError if the field is required but the value is {@code null}.
     */
    @Override
    public Object validate(Object value, String fieldName, Annotation annotation) throws ValidationError {
        ValidateField validateField = (ValidateField) annotation;
        boolean required = validateField.required();

        if (required && value == null) {
            throw new ValidationError("Field '%s' is required but not provided.".formatted(fieldName));
        }

        return value;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Class<? extends Annotation> getSupportedAnnotation() {
        return ValidateField.class;
    }
}
