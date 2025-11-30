package io.github.anvil.validation;

import java.lang.annotation.Annotation;

/**
 * Contract for all annotation-based validators used by Anvil.
 */
public interface Validator {

    /**
     * Validates the given value for the specified field using the provided annotation.
     *
     * @param value      the value to validate.
     * @param fieldName  the name of the field being validated.
     * @param annotation the annotation that defines the validation rule.
     * @return an optional transformed value (it may be {@code null}) to be used instead of the original.
     * @throws ValidationError if validation fails.
     */
    Object validate(Object value, String fieldName, Annotation annotation) throws ValidationError;

    /**
     * Returns the annotation type this validator supports.
     *
     * @return the supported annotation type.
     */
    Class<? extends Annotation> getSupportedAnnotation();
}
