package io.github.anvil.annotations.numeric;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Validates that a numeric field is exactly equal to the configured value.
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Equal {

    /**
     * The exact value the annotated field must equal.
     *
     * @return the required numeric value.
     */
    double value();
}
