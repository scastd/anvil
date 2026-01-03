package io.github.anvil.validation.validators;

import io.github.anvil.Schema;
import io.github.anvil.annotations.Inner;
import io.github.anvil.exceptions.ValidationException;
import io.github.anvil.processor.Processor;
import io.github.anvil.validation.ValidationError;
import io.github.anvil.validation.Validator;

import java.lang.annotation.Annotation;
import java.util.List;

/**
 * Validator implementation for the {@link Inner} annotation.
 *
 * <p>This validator handles nested schema validation by processing the field's value as a nested
 * schema instance. When a field is annotated with {@code @Inner(SchemaClass.class)}, this validator
 * extracts the schema class from the annotation and uses the processor to validate and construct
 * the nested schema instance.</p>
 *
 * <p>The validator is registered dynamically by the processor when it encounters fields annotated
 * with {@link Inner}. This allows nested schemas to be validated recursively, ensuring that all
 * validation rules defined in the nested schema class are applied.</p>
 *
 * <p>If validation of the nested schema fails, a {@link ValidationException} is thrown, which
 * will be handled according to the parent schema's fail-fast configuration.</p>
 *
 * @see Inner
 * @see Validator
 * @see Processor
 */
public class InnerValidator implements Validator {
    private final Processor<?> processor;

    /**
     * Creates a new instance with the specified processor.
     *
     * @param processor the processor to use for validating nested schemas.
     */
    public InnerValidator(Processor<?> processor) {
        this.processor = processor;
    }

    /**
     * Validates the given value as a nested schema instance.
     *
     * <p>This method extracts the schema class from the {@link Inner} annotation and uses the
     * processor to validate and construct the nested schema. The nested validation follows the
     * same rules as top-level schema validation.</p>
     *
     * <p>When nested validation fails, all errors are extracted and prefixed with the field path
     * (e.g., "address.street" instead of just "street") to provide clear context about which
     * nested field has the error. All nested errors are preserved and thrown as a
     * {@link ValidationException} (unchecked) so they can all be collected by the processor.</p>
     *
     * @param value      the nested input value to validate.
     * @param fieldName  the name of the field being validated (used for error messages).
     * @param annotation the {@link Inner} annotation instance.
     * @return the validated and constructed schema instance.
     * @throws ValidationError     if validation of the nested schema fails (for single errors).
     * @throws ValidationException if validation of the nested schema fails with multiple errors.
     *                             The exception contains all nested errors with field paths prefixed.
     */
    @Override
    public Schema validate(Object value, String fieldName, Annotation annotation) throws ValidationError {
        Inner innerAnnotation = (Inner) annotation;
        Class<? extends Schema> schemaClass = innerAnnotation.value();

        try {
            return this.processor.processUnchecked(value, schemaClass);
        } catch (ValidationException e) {
            // Extract all nested errors and prefix them with the field path using dot notation
            // This preserves all nested validation errors and builds the full path from the root
            // Format: "root.parent.nestedField: error message" for better readability
            List<ValidationError> prefixedErrors = e.getErrors()
                                                    .stream()
                                                    .map(error -> new ValidationError(
                                                        prefixErrorMessage(error.getMessage(), fieldName)))
                                                    .toList();

            throw new ValidationException(prefixedErrors);
        }
    }

    /**
     * Prefixes an error message with the parent field name to build the full path from the root.
     *
     * <p>This method replaces "for field 'fieldName'" with "for field 'parentField.fieldName'"
     * to show the complete path. It handles both simple field names and already-prefixed paths
     * (for deeply nested structures).</p>
     *
     * @param errorMessage    the original error message.
     * @param parentFieldName the parent field name to prefix.
     * @return the error message with the parent field name prefixed.
     */
    private String prefixErrorMessage(String errorMessage, String parentFieldName) {
        String prefixedMessage = errorMessage.replaceFirst(
            "for field '([^']+)'",
            "for field '%s.$1'".formatted(parentFieldName)
        );
        // If the replacement didn't match (different error message format),
        // prefix the entire message with the parent field name
        if (prefixedMessage.equals(errorMessage)) {
            prefixedMessage = "%s: %s".formatted(parentFieldName, errorMessage);
        }
        return prefixedMessage;
    }

    /**
     * Returns the annotation type supported by this validator.
     *
     * @return {@link Inner} class.
     */
    @Override
    public Class<? extends Annotation> getSupportedAnnotation() {
        return Inner.class;
    }
}
