package io.github.anvil.annotations.numeric;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Validates that a numeric field is contained in the configured set of values.
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface In {

    /**
     * The allowed numeric values for the annotated field.
     *
     * @return the whitelist of acceptable numeric values.
     */
    double[] value();
}
