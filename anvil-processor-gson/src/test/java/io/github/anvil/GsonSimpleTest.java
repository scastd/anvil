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

package io.github.anvil;

import com.google.gson.JsonObject;
import io.github.anvil.annotations.OptionalValue;
import io.github.anvil.annotations.StrEqual;
import io.github.anvil.annotations.Validate;
import io.github.anvil.annotations.numeric.Equal;
import io.github.anvil.processor.GsonProcessor;
import org.junit.jupiter.api.Test;

import static io.github.anvil.TestUtils.getJsonObject;
import static org.assertj.core.api.Assertions.assertThat;

class GsonSimpleTest {
    @Validate
    public static class NoArgsConstructorClass implements Schema {
        @Equal(10.2f)
        Float floatField;

        @Equal(10.2d)
        double doubleField;

        @Equal(10)
        int intField;

        @Equal(10)
        short shortField;

        @OptionalValue
        @StrEqual("test")
        String stringField;
    }

    @Test
    void processNoArgsConstructor() {
        Anvil<JsonObject> anvil = new Anvil<>(new GsonProcessor());

        JsonObject json = getJsonObject("""
                                            {
                                                "floatField": 10.2,
                                                "doubleField": 10.2,
                                                "intField": 10,
                                                "shortField": 10
                                            }
                                            """);

        NoArgsConstructorClass validated = anvil.validate(json, NoArgsConstructorClass.class);
        assertThat(validated.floatField).isEqualTo(10.2f);
        assertThat(validated.doubleField).isEqualTo(10.2d);
        assertThat(validated.intField).isEqualTo(10);
        assertThat(validated.shortField).isEqualTo((short) 10);
        assertThat(validated.stringField).isNull();
    }

    @Validate
    public static class AllArgsConstructorClass implements Schema {
        @Equal(10.2f)
        Float floatField;

        @Equal(10.2d)
        double doubleField;

        @Equal(10)
        int intField;

        @Equal(10)
        short shortField;

        @OptionalValue
        @StrEqual("test")
        String stringField;

        public AllArgsConstructorClass(
            Float floatField,
            double doubleField,
            int intField,
            short shortField,
            String stringField
        ) {
            this.floatField = floatField;
            this.doubleField = doubleField;
            this.intField = intField;
            this.shortField = shortField;
            this.stringField = stringField;
        }
    }

    @Test
    void processAllArgsConstructor() {
        Anvil<JsonObject> anvil = new Anvil<>(new GsonProcessor());

        JsonObject json = getJsonObject("""
                                            {
                                                "floatField": 10.2,
                                                "doubleField": 10.2,
                                                "intField": 10,
                                                "shortField": 10
                                            }
                                            """);

        AllArgsConstructorClass validated = anvil.validate(json, AllArgsConstructorClass.class);
        assertThat(validated.floatField).isEqualTo(10.2f);
        assertThat(validated.doubleField).isEqualTo(10.2d);
        assertThat(validated.intField).isEqualTo(10);
        assertThat(validated.shortField).isEqualTo((short) 10);
        assertThat(validated.stringField).isNull();
    }

    @Validate
    public record TestRecord(
        @Equal(10.2f)
        Float floatField,

        @Equal(10.2d)
        double doubleField,

        @Equal(10)
        int intField,

        @Equal(10)
        short shortField,

        @OptionalValue
        @StrEqual("test")
        String stringField
    ) implements Schema {
    }

    @Test
    void processRecord() {
        Anvil<JsonObject> anvil = new Anvil<>(new GsonProcessor());

        JsonObject json = getJsonObject("""
                                            {
                                                "floatField": 10.2,
                                                "doubleField": 10.2,
                                                "intField": 10,
                                                "shortField": 10
                                            }
                                            """);

        TestRecord validated = anvil.validate(json, TestRecord.class);
        assertThat(validated.floatField).isEqualTo(10.2f);
        assertThat(validated.doubleField).isEqualTo(10.2d);
        assertThat(validated.intField).isEqualTo(10);
        assertThat(validated.shortField).isEqualTo((short) 10);
        assertThat(validated.stringField).isNull();
    }
}
