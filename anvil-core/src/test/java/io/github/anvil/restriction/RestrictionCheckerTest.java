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
            .hasMessage("Field 'fieldViolates' violates annotation restrictions: [Equal, Greater]");
    }
}
