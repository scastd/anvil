package io.github.anvil.annotations;

import io.github.anvil.validation.validators.StringComparer.StringComparisonStrategy;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Validates that a {@link String} field equals a specific value.
 *
 * <p>The comparison behavior can be customized through {@link #strategy()}.</p>
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface StrEqual {

    /**
     * The expected value the annotated field must match.
     *
     * @return the required string value.
     */
    String value();

    /**
     * The comparison strategy to use when comparing the field value against
     * the configured {@link #value()}.
     *
     * @return the string comparison strategy.
     */
    StringComparisonStrategy strategy() default StringComparisonStrategy.CASE_SENSITIVE;
}
