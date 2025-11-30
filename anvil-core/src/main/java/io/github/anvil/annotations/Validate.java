package io.github.anvil.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marks a class as eligible for validation by the Anvil validation engine.
 *
 * <p>This annotation controls whether an object should be validated and how
 * additional diagnostic information is handled.</p>
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Validate {

    /**
     * Enables or disables validation for the annotated type.
     *
     * @return {@code true} if validation is enabled, {@code false} otherwise.
     */
    boolean value() default true;

    /**
     * Indicates whether additional informational messages should be printed
     * during validation.
     *
     * @return {@code true} to print extra diagnostic information.
     */
    boolean printInfo() default false;

    /**
     * Indicates whether validation should stop at the first failure.
     *
     * @return {@code true} to fail fast, {@code false} to collect all violations.
     */
    boolean failFast() default false;
}
