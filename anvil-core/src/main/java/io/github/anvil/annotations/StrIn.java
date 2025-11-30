package io.github.anvil.annotations;

import io.github.anvil.validation.validators.StringComparer.StringComparisonStrategy;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Validates that a {@link String} field matches one of a predefined set of values.
 *
 * <p>The comparison behavior can be customized through {@link #strategy()}.</p>
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface StrIn {

    /**
     * The allowed string values for the annotated field.
     *
     * @return the whitelist of acceptable values.
     */
    String[] value();

    /**
     * The comparison strategy to use when checking the field value against
     * the configured {@link #value()} list.
     *
     * @return the string comparison strategy.
     */
    StringComparisonStrategy strategy() default StringComparisonStrategy.CASE_SENSITIVE;
}
