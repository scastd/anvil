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

import io.github.anvil.annotations.Dated;
import io.github.anvil.validation.ValidationError;
import io.github.anvil.validation.Validator;

import java.lang.annotation.Annotation;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeParseException;
import java.time.temporal.Temporal;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * Validator for the {@link Dated} annotation, ensuring a string is recognized as the specified date/time
 * formatter.
 */
public class DatedValidator implements Validator {
    private static final Map<Class<? extends Temporal>, Function<String, ? extends Temporal>> PARSERS = new HashMap<>();

    static {
        PARSERS.put(Instant.class, Instant::parse);
        PARSERS.put(LocalDate.class, LocalDate::parse);
        PARSERS.put(LocalDateTime.class, LocalDateTime::parse);
        PARSERS.put(ZonedDateTime.class, ZonedDateTime::parse);
        PARSERS.put(OffsetDateTime.class, OffsetDateTime::parse);
    }

    /**
     * Validates that the given string value corresponds to a date/time format.
     *
     * @param value      the value to validate.
     * @param fieldName  the name of the field being validated.
     * @param annotation the {@link Dated} annotation instance.
     * @return the instance of the {@link Temporal} representing this string.
     * @throws ValidationError if the {@link Temporal} class is not yet supported.
     */
    @Override
    public Object validate(Object value, String fieldName, Annotation annotation) throws ValidationError {
        if (value == null) {
            throw new ValidationError("for field '%s': Value cannot be null".formatted(fieldName));
        }

        Dated dated = (Dated) annotation;
        Class<? extends Temporal> datedClass = dated.value();
        String stringValue = (String) value;

        var parser = PARSERS.get(datedClass);

        if (parser == null) {
            throw new ValidationError(
                "for field '%s': Class '%s' is not yet allowed to be converted".formatted(
                    fieldName, datedClass.getSimpleName()));
        }

        try {
            return parser.apply(stringValue);
        } catch (DateTimeParseException e) {
            throw new ValidationError(
                "for field '%s': Value '%s' is not a valid %s format".formatted(
                    fieldName, stringValue, datedClass.getSimpleName()));
        }
    }

    @Override
    public Class<? extends Annotation> getSupportedAnnotation() {
        return Dated.class;
    }
}
