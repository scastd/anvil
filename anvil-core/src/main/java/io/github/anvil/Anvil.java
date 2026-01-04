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

package io.github.anvil;

import io.github.anvil.exceptions.ValidationException;
import io.github.anvil.processor.Processor;
import io.github.anvil.validation.ValidationError;

/**
 * Entry point for validating inputs and building {@link Schema} instances.
 *
 * <p>An {@code Anvil} instance delegates to a {@link Processor} to read values from the
 * input type {@code IN}, apply all validation rules, and construct the target schema.</p>
 *
 * @param <IN> the input type from which field values are read.
 */
public record Anvil<IN>(Processor<IN> processor) {

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
    public <S extends Schema> S validate(IN in, Class<S> clazz) {
        return this.processor.process(in, clazz);
    }
}
