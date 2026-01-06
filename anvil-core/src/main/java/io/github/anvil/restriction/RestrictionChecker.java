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

package io.github.anvil.restriction;

import io.github.anvil.annotations.numeric.Equal;
import io.github.anvil.annotations.numeric.Greater;
import io.github.anvil.annotations.numeric.GreaterOrEqual;
import io.github.anvil.annotations.numeric.Less;
import io.github.anvil.annotations.numeric.LessOrEqual;
import io.github.anvil.exceptions.FieldViolatesRestrictionsException;

import java.lang.reflect.Field;
import java.util.List;

/**
 * Applies {@link Restriction} rules to fields and throws errors when
 * invalid annotation combinations are detected.
 */
public class RestrictionChecker {
    private static final List<Restriction> restrictions;

    static {
        restrictions = List.of(
            new Restriction(Equal.class, Greater.class),
            new Restriction(Equal.class, Less.class),
            new Restriction(Greater.class, GreaterOrEqual.class),
            new Restriction(Less.class, LessOrEqual.class)
        );
    }

    /**
     * Creates a new {@link RestrictionChecker} instance.
     */
    public RestrictionChecker() {
    }

    /**
     * Verifies that the annotations present on the given field do not violate
     * any configured {@link Restriction}.
     *
     * @param field the field whose annotations should be checked.
     * @throws FieldViolatesRestrictionsException if a restriction is violated.
     */
    public void checkAnnotationRestrictions(Field field) {
        restrictions.forEach(restriction -> {
            boolean hasAllAnnotations = restriction.annotations()
                                                   .stream()
                                                   .map(field::isAnnotationPresent)
                                                   .reduce((a, b) -> a && b)
                                                   .orElse(false);

            if (hasAllAnnotations) {
                throw new FieldViolatesRestrictionsException(field.getName(), restriction);
            }
        });
    }
}
