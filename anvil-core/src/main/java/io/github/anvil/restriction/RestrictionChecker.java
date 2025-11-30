package io.github.anvil.restriction;

import io.github.anvil.annotations.numeric.Equal;
import io.github.anvil.annotations.numeric.Greater;
import io.github.anvil.annotations.numeric.Less;
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
            new Restriction(Equal.class, Less.class)
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
        for (var restriction : restrictions) {
            boolean hasAllAnnotations = true;

            for (var annotation : restriction.annotations()) {
                if (!field.isAnnotationPresent(annotation)) {
                    hasAllAnnotations = false;
                    break;
                }
            }

            if (hasAllAnnotations) {
                throw new FieldViolatesRestrictionsException(field.getName(), restriction);
            }
        }
    }
}
