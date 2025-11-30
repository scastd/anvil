package io.github.anvil.annotations.numeric;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Validates that a numeric field is less than or equal to the configured value.
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface LessOrEqual {

    /**
     * The maximum allowed value (inclusive).
     *
     * @return the upper (inclusive) bound.
     */
    float value();
}
