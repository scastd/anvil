/*
 * Copyright 2025-present Samuel Castrillo
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
