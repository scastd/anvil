package io.github.anvil.validation.validators;

import io.github.anvil.annotations.OptionalValue;
import io.github.anvil.validation.ValidationError;
import io.github.anvil.validation.Validator;

import java.lang.annotation.Annotation;

/**
 * Validator for the {@link OptionalValue} annotation.
 *
 * <p>This validator does not enforce any additional constraints; it simply passes the
 * value through. The optional behavior is handled at the processor level.</p>
 */
public class OptionalValueValidator implements Validator {

    /**
     * Returns the value as-is, without performing any checks.
     *
     * @param value      the value to validate.
     * @param fieldName  the name of the field being validated.
     * @param annotation the {@link OptionalValue} annotation instance.
     * @return the original value, unchanged.
     * @throws ValidationError never; this validator does not raise validation errors.
     */
    @Override
    public Object validate(Object value, String fieldName, Annotation annotation) throws ValidationError {
        // Since the field is optional, we simply return the value as is (null or non-null)
        return value;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Class<? extends Annotation> getSupportedAnnotation() {
        return OptionalValue.class;
    }
}
