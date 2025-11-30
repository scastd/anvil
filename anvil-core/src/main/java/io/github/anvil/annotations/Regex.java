package io.github.anvil.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Validates that a {@link String} field matches the given regular expression.
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Regex {

    /**
     * The regular expression pattern that the annotated field must match.
     *
     * @return the regex pattern.
     */
    String value();
}
