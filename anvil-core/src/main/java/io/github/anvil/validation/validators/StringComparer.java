package io.github.anvil.validation.validators;

import java.util.Objects;

/**
 * Utility class for comparing strings using different {@link StringComparisonStrategy} options.
 */
public class StringComparer {

    /**
     * Compares two strings using the given strategy.
     *
     * @param a        the first string to compare.
     * @param b        the second string to compare.
     * @param strategy the comparison strategy to apply.
     * @return {@code true} if the strings are considered equal under the strategy, {@code false} otherwise.
     */
    public static boolean equal(String a, String b, StringComparisonStrategy strategy) {
        if (a == null || b == null) {
            return Objects.equals(a, b);
        }

        return switch (strategy) {
            case CASE_SENSITIVE -> a.equals(b);
            case CASE_INSENSITIVE -> a.equalsIgnoreCase(b);
        };
    }

    /**
     * Strategies for comparing strings.
     */
    public enum StringComparisonStrategy {
        CASE_INSENSITIVE,
        CASE_SENSITIVE
    }
}
