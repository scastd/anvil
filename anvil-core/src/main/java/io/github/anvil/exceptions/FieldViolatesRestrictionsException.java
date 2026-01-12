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

package io.github.anvil.exceptions;

import io.github.anvil.restriction.Restriction;

/**
 * Thrown when a field's annotation configuration violates Anvil's
 * {@link Restriction} rules.
 */
public class FieldViolatesRestrictionsException extends IllegalStateException {

    /**
     * Creates a new exception for a field whose annotations do not satisfy
     * the defined {@link Restriction}s.
     *
     * @param fieldName   the name of the offending field.
     * @param restriction the violated restriction description.
     */
    public FieldViolatesRestrictionsException(String fieldName, Restriction restriction) {
        super(
            "Field '%s' violates annotation restrictions: [%s].".formatted(fieldName, getRestrictionNames(restriction))
        );
    }

    /**
     * Builds a comma-separated list of simple annotation names for the given restriction.
     *
     * @param restriction the restriction whose annotation types should be listed.
     * @return a comma-separated list of annotation simple names.
     */
    private static String getRestrictionNames(Restriction restriction) {
        return String.join(
            ", ",
            restriction.annotations().stream().map(Class::getSimpleName).toArray(String[]::new)
        );
    }
}
