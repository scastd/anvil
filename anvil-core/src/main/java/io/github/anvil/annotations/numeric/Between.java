package io.github.anvil.annotations.numeric;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Validates that a numeric field is within the half-open interval
 * {@code [min, max)}, that is:
 * <ul>
 *   <li>values <strong>greater than or equal to</strong> {@link #min()} are allowed.</li>
 *   <li>values <strong>strictly less than</strong> {@link #max()} are allowed.</li>
 * </ul>
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Between {

    /**
     * The minimum allowed value (inclusive).
     *
     * @return the lower bound of the valid range.
     */
    float min();

    /**
     * The maximum allowed value (exclusive).
     *
     * @return the upper bound of the valid range (not included).
     */
    float max();
}
