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

package io.github.anvil.processor;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * {@link io.github.anvil.processor.Processor} implementation for Jackson's {@link JsonNode}.
 *
 * <p>This processor knows how to extract booleans, numbers, and strings from an {@link ObjectNode}
 * so that Anvil can validate and build schema instances from Jackson-based JSON inputs.</p>
 *
 * <p>The processor handles nested objects and arrays. When extracting nested input via
 * {@link #getInnerInput(JsonNode, String)}, it returns {@link com.fasterxml.jackson.databind.node.ArrayNode}
 * instances for array fields, enabling support for fields annotated with {@link io.github.anvil.annotations.List}.</p>
 */
public class JacksonProcessor extends Processor<JsonNode> {
    private static final Logger logger = LoggerFactory.getLogger(JacksonProcessor.class);

    /**
     * {@inheritDoc}
     */
    @Override
    public Boolean getBooleanFieldValue(JsonNode input, String fieldName) {
        return input.get(fieldName).asBoolean();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Number getNumberFieldValue(JsonNode input, Class<?> numberClass, String fieldName) {
        JsonNode element = input.get(fieldName);

        return switch (numberClass.getSimpleName()) {
            case "Integer", "int" -> element.intValue();
            case "Long", "long" -> element.longValue();
            case "Float", "float" -> element.floatValue();
            case "Double", "double" -> element.doubleValue();
            case "Short", "short" -> element.shortValue();
            case "Byte", "byte" -> element.numberValue().byteValue();
            default -> throw new IllegalArgumentException("Unsupported number class: " + numberClass.getName() + ".");
        };
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getStringFieldValue(JsonNode input, String fieldName) {
        return input.get(fieldName).asText();
    }

    /**
     * Extracts a nested input representation for a field marked as an inner schema or list.
     *
     * <p>This method extracts the field value from the parent {@link ObjectNode}. If the field
     * contains a JSON array, it returns the {@link com.fasterxml.jackson.databind.node.ArrayNode}
     * directly, which allows the {@link io.github.anvil.validation.validators.ListValidator} to
     * process list fields annotated with {@link io.github.anvil.annotations.List}. For nested objects,
     * it returns the {@link JsonNode} representing the nested structure.</p>
     *
     * @param input     the input source (must be an {@link ObjectNode}).
     * @param fieldName the name of the field to read.
     * @return the nested input representation (ArrayNode for arrays, JsonNode for objects),
     *         or {@code null} if the field is not present.
     */
    @Override
    public JsonNode getInnerInput(JsonNode input, String fieldName) {
        JsonNode jsonNode = input.get(fieldName);

        if (jsonNode instanceof ArrayNode arrayNode) {
            return arrayNode;
        }

        return jsonNode;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Logger getLogger() {
        return logger;
    }
}
