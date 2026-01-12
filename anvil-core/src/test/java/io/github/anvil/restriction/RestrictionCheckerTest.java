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
import io.github.anvil.exceptions.FieldViolatesRestrictionsException;
import io.github.anvil.utils.ReflectionUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class RestrictionCheckerTest {
    private RestrictionChecker restrictionChecker;

    @BeforeEach
    void setUp() {
        this.restrictionChecker = new RestrictionChecker();
    }

    @SuppressWarnings("unused")
    static class EqualGreaterRestriction {
        @Equal(2)
        private int field;

        @Equal(2)
        @Greater(1)
        private int fieldViolates;
    }

    @Test
    void testCheckAnnotationRestrictions() {
        Field field = ReflectionUtils.getField(EqualGreaterRestriction.class, "field");

        this.restrictionChecker.checkAnnotationRestrictions(field);
    }

    @Test
    void testCheckAnnotationRestrictionsThrows() {
        Field field = ReflectionUtils.getField(EqualGreaterRestriction.class, "fieldViolates");

        assertThatThrownBy(() -> this.restrictionChecker.checkAnnotationRestrictions(field))
            .isInstanceOf(FieldViolatesRestrictionsException.class)
            .hasMessage("Field 'fieldViolates' violates annotation restrictions: [Equal, Greater].");
    }
}
