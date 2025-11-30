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
            "Field '%s' violates annotation restrictions: [%s]".formatted(fieldName, getRestrictionNames(restriction))
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
