package io.github.anvil.annotations.numeric;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Validates that a numeric field is strictly greater than the configured value.
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Greater {

    /**
     * The threshold that the annotated field must be greater than.
     *
     * @return the lower (exclusive) bound.
     */
    float value();
}
