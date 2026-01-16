/*
 * Copyright 2025-present Samuel Castrillo
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.github.anvil.validation;

import io.github.anvil.validation.validators.EnumValueValidator;
import io.github.anvil.validation.validators.OptionalValueValidator;
import io.github.anvil.validation.validators.RegexValidator;
import io.github.anvil.validation.validators.StrEqualValidator;
import io.github.anvil.validation.validators.StrInValidator;
import io.github.anvil.validation.validators.UUIDValidator;
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
    private final Map<Class<? extends Annotation>, Validator> validators = new HashMap<>();
    private static final ValidatorRegistry INSTANCE = new ValidatorRegistry();

    /**
     * Returns the singleton {@link ValidatorRegistry} instance.
     *
     * @return the shared validator registry instance.
     */
    public static ValidatorRegistry getInstance() {
        return INSTANCE;
    }

    private ValidatorRegistry() {
        this.registerBuiltinValidators();
    }

    /**
     * Builds the list of all built-in validators.
     *
     * @return an immutable list of validator instances.
     */
    private static List<Validator> getBuiltinValidators() {
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
            new OptionalValueValidator(),
            new UUIDValidator()
        );
    }

    /**
     * Registers all built-in validators in the registry.
     */
    private void registerBuiltinValidators() {
        getBuiltinValidators().forEach(validator -> this.validators.put(validator.getSupportedAnnotation(), validator));
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
            throw new IllegalArgumentException("No validator found for annotation: " + annotationClass.getName() + ".");
        }

        return validator;
    }

    /**
     * Registers a new validator for its supported annotation type.
     *
     * <p>If a validator is already registered for the annotation type, it will be replaced
     * with the new validator.</p>
     *
     * @param validator the validator instance to register.
     */
    public void addValidator(Validator validator) {
        this.validators.put(validator.getSupportedAnnotation(), validator);
    }

    /**
     * Registers a validator only if no validator is already registered for its supported annotation type.
     *
     * <p>Unlike {@link #addValidator(Validator)}, this method will not override an existing validator.
     * If a validator is already registered for the annotation type, this method does nothing and
     * the existing validator remains registered.</p>
     *
     * @param validator the validator instance to register.
     */
    public void addNonOverridingValidator(Validator validator) {
        this.validators.putIfAbsent(validator.getSupportedAnnotation(), validator);
    }
}
