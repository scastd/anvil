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


import io.github.anvil.Schema;
import io.github.anvil.annotations.Dated;
import io.github.anvil.annotations.Validate;
import io.github.anvil.validation.ValidationError;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.lang.annotation.Annotation;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.YearMonth;
import java.time.ZonedDateTime;

import static io.github.anvil.utils.ReflectionUtils.getFieldAnnotation;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class DatedValidatorTest {
    private final DatedValidator validator = new DatedValidator();

    @Validate
    @SuppressWarnings("unused")
    static class DatedTestSchema implements Schema {
        @Dated(Instant.class)
        Instant instant;

        @Dated(LocalDate.class)
        LocalDate localDate;

        @Dated(LocalDateTime.class)
        LocalDateTime localDateTime;

        @Dated(ZonedDateTime.class)
        ZonedDateTime zonedDateTime;

        @Dated(OffsetDateTime.class)
        OffsetDateTime offsetDateTime;

        @Dated(YearMonth.class)
        YearMonth yearMonth; // Used for testing unsupported types
    }

    @Test
    void testValidateInstant() throws ValidationError {
        Annotation annotation = getFieldAnnotation(DatedTestSchema.class, "instant", Dated.class);
        Object returnedValue = this.validator.validate("2026-07-11T22:58:48.232Z", "instant", annotation);
        assertThat(returnedValue).isEqualTo(Instant.parse("2026-07-11T22:58:48.232Z"));
    }

    @Test
    void testValidateLocalDate() throws ValidationError {
        Annotation annotation = getFieldAnnotation(DatedTestSchema.class, "localDate", Dated.class);
        Object returnedValue = this.validator.validate("2026-07-11", "localDate", annotation);
        assertThat(returnedValue).isEqualTo(LocalDate.of(2026, 7, 11));
    }

    @Test
    void testValidateLocalDateTime() throws ValidationError {
        Annotation annotation = getFieldAnnotation(DatedTestSchema.class, "localDateTime", Dated.class);
        Object returnedValue = this.validator.validate("2026-07-11T14:30:00", "localDateTime", annotation);
        assertThat(returnedValue).isEqualTo(LocalDateTime.of(2026, 7, 11, 14, 30, 0));
    }

    @Test
    void testValidateZonedDateTime() throws ValidationError {
        Annotation annotation = getFieldAnnotation(DatedTestSchema.class, "zonedDateTime", Dated.class);
        Object returnedValue = this.validator.validate("2026-07-11T14:30:00+02:00[Europe/Madrid]", "zonedDateTime",
                                                       annotation);
        assertThat(returnedValue).isEqualTo(ZonedDateTime.parse("2026-07-11T14:30:00+02:00[Europe/Madrid]"));
    }

    @Test
    void testValidateOffsetDateTime() throws ValidationError {
        Annotation annotation = getFieldAnnotation(DatedTestSchema.class, "offsetDateTime", Dated.class);
        Object returnedValue = this.validator.validate("2026-07-11T14:30:00-05:00", "offsetDateTime", annotation);
        assertThat(returnedValue).isEqualTo(OffsetDateTime.parse("2026-07-11T14:30:00-05:00"));
    }

    @Test
    void testGetSupportedAnnotation() {
        assertThat(this.validator.getSupportedAnnotation()).isEqualTo(Dated.class);
    }

    @Test
    void testValidateUnsupportedTemporalClassThrowsValidationError() {
        Annotation annotation = getFieldAnnotation(DatedTestSchema.class, "yearMonth", Dated.class);

        assertThatThrownBy(() -> this.validator.validate("2026-07", "yearMonth", annotation))
            .isInstanceOf(ValidationError.class)
            .hasMessage("for field 'yearMonth': Class 'YearMonth' is not yet allowed to be converted");
    }

    @ParameterizedTest
    @ValueSource(strings = {
        "invalid-date-string",
        "2026-99-99T00:00:00Z", // Invalid calendar date
        "1783810593",           // Unix timestamp (expects ISO-8601)
        ""                      // Empty string
    })
    void testValidateMalformedDateStringThrowsValidationError(String invalidDate) {
        Annotation annotation = getFieldAnnotation(DatedTestSchema.class, "instant", Dated.class);

        assertThatThrownBy(() -> this.validator.validate(invalidDate, "instant", annotation))
            .isInstanceOf(ValidationError.class)
            .hasMessageContaining("is not a valid Instant format");
    }

    @Test
    void testValidateNullValueThrowsValidationError() {
        Annotation annotation = getFieldAnnotation(DatedTestSchema.class, "instant", Dated.class);

        assertThatThrownBy(() -> this.validator.validate(null, "instant", annotation))
            .isInstanceOf(ValidationError.class)
            .hasMessage("for field 'instant': Value cannot be null");
    }
}
