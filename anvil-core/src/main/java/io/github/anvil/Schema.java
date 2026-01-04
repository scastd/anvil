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
