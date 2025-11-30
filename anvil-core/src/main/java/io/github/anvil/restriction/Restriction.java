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
