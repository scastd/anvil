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

import java.lang.annotation.Annotation;
import java.util.List;

/**
 * Describes a combination of annotation types that are not allowed to appear together.
 *
 * <p>Restrictions are evaluated by {@link RestrictionChecker} against field annotations.</p>
 *
 * @param annotations the list of annotation types that must not coexist.
 */
public record Restriction(List<Class<? extends Annotation>> annotations) {

    /**
     * Creates a new restriction from the given annotation types.
     *
     * @param annotations the annotation types that must not coexist.
     */
    @SafeVarargs
    public Restriction(Class<? extends Annotation>... annotations) {
        this(List.of(annotations));
    }
}
