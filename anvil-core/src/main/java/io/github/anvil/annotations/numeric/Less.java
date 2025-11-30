package io.github.anvil.annotations.numeric;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Validates that a numeric field is strictly less than the configured value.
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Less {

    /**
     * The threshold that the annotated field must be less than.
     *
     * @return the upper (exclusive) bound.
     */
    float value();
}
