package io.github.anvil.validation;

import io.github.anvil.validation.validators.EnumValueValidator;
import io.github.anvil.validation.validators.RegexValidator;
import io.github.anvil.validation.validators.StrEqualValidator;
import io.github.anvil.validation.validators.StrInValidator;
import io.github.anvil.validation.validators.ValidateFieldValidator;
import io.github.anvil.validation.validators.numeric.BetweenValidator;
import io.github.anvil.validation.validators.numeric.EqualValidator;
import io.github.anvil.validation.validators.numeric.GreaterOrEqualValidator;
import io.github.anvil.validation.validators.numeric.GreaterValidator;
import io.github.anvil.validation.validators.numeric.InValidator;
import io.github.anvil.validation.validators.numeric.LessOrEqualValidator;
import io.github.anvil.validation.validators.numeric.LessValidator;

import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Central registry mapping annotation types to their corresponding {@link Validator} instances.
 *
 * <p>This is implemented as a singleton and pre-populated with all built-in validators.</p>
 */
public final class ValidatorRegistry {
    private static final Map<Class<? extends Annotation>, Validator> validators = new HashMap<>();
    private static final ValidatorRegistry INSTANCE = new ValidatorRegistry();

    static {
        for (Validator validator : getValidatorsList()) {
            validators.put(validator.getSupportedAnnotation(), validator);
        }
    }

    /**
     * Returns the singleton {@link ValidatorRegistry} instance.
     *
     * @return the shared validator registry instance.
     */
    public static ValidatorRegistry getInstance() {
        return INSTANCE;
    }

    private ValidatorRegistry() {
        // Prevent instantiation
    }

    /**
     * Builds the list of all built-in validators.
     *
     * @return an immutable list of validator instances.
     */
    private static List<Validator> getValidatorsList() {
        return List.of(
            new BetweenValidator(),
            new EqualValidator(),
            new GreaterOrEqualValidator(),
            new GreaterValidator(),
            new InValidator(),
            new LessOrEqualValidator(),
            new LessValidator(),
            new EnumValueValidator(),
            new StrEqualValidator(),
            new StrInValidator(),
            new RegexValidator(),
            new ValidateFieldValidator()
        );
    }

    /**
     * Returns the validator registered for the given annotation type.
     *
     * @param annotationClass the annotation type to look up.
     * @return the matching validator instance.
     * @throws IllegalArgumentException if no validator is registered for the annotation type.
     */
    public Validator getValidator(Class<? extends Annotation> annotationClass) {
        var validator = validators.get(annotationClass);

        if (validator == null) {
            throw new IllegalArgumentException("No validator found for annotation: " + annotationClass.getName());
        }

        return validator;
    }

    /**
     * Registers a new validator for its supported annotation type.
     *
     * @param validator the validator instance to register.
     */
    public void addValidator(Validator validator) {
        validators.put(validator.getSupportedAnnotation(), validator);
    }
}
