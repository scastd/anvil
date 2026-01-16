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

import com.fasterxml.jackson.databind.JsonNode;
import io.github.anvil.annotations.EnumValue;
import io.github.anvil.annotations.List;
import io.github.anvil.annotations.Validate;
import io.github.anvil.annotations.numeric.Equal;
import io.github.anvil.processor.JacksonProcessor;
import io.github.anvil.validation.validators.StringComparer.StringComparisonStrategy;
import org.junit.jupiter.api.Test;

import static io.github.anvil.TestUtils.getObjectNode;
import static org.assertj.core.api.Assertions.assertThat;

class JacksonSimpleTest {
    @Validate
    public static class A implements Schema {
        @Equal(10.2f)
        Float floatField;

        @Equal(10.2d)
        double doubleField;

        @Equal(10)
        int intField;

        @Equal(10)
        short shortField;

        @EnumValue(StringComparisonStrategy.class)
        StringComparisonStrategy stringComparisonStrategy;
    }

    @Test
    void process() {
        Anvil<JsonNode> anvil = new Anvil<>(new JacksonProcessor());

        JsonNode json = getObjectNode("""
                                          {
                                              "floatField": 10.2,
                                              "doubleField": 10.2,
                                              "intField": 10,
                                              "shortField": 10,
                                              "stringComparisonStrategy": "CASE_SENSITIVE"
                                          }
                                          """);

        A a = anvil.validate(json, A.class);
        assertThat(a.floatField).isEqualTo(10.2f);
        assertThat(a.doubleField).isEqualTo(10.2d);
        assertThat(a.intField).isEqualTo(10);
        assertThat(a.shortField).isEqualTo((short) 10);
        assertThat(a.stringComparisonStrategy).isEqualTo(StringComparisonStrategy.CASE_SENSITIVE);
    }

    @Validate
    public record Element(
        @Equal(10.2f)
        Float floatField
    ) implements Schema {
    }

    @Validate
    public record RecordWithArray(
        @List(Element.class)
        java.util.List<Element> elements
    ) implements Schema {
    }

    @Test
    void processRecordWithArray() {
        Anvil<JsonNode> anvil = new Anvil<>(new JacksonProcessor());

        JsonNode json = getObjectNode("""
                                          {
                                              "elements": [
                                                  { "floatField": 10.2 },
                                                  { "floatField": 10.2 }
                                              ]
                                          }
                                          """);

        RecordWithArray validated = anvil.validate(json, RecordWithArray.class);
        assertThat(validated.elements).hasSize(2);
        for (Element element : validated.elements) {
            assertThat(element.floatField).isEqualTo(10.2f);
        }
    }
}
