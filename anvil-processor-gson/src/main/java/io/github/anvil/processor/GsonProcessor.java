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

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * {@link io.github.anvil.processor.Processor} implementation for Gson's {@link JsonElement}.
 *
 * <p>This processor knows how to extract booleans, numbers, and strings from a {@link JsonObject}
 * so that Anvil can validate and build schema instances from Gson-based JSON inputs.</p>
 *
 * <p>The processor handles nested objects and arrays. When extracting nested input via
 * {@link #getInnerInput(JsonElement, String)}, it returns {@link com.google.gson.JsonArray}
 * instances for array fields, enabling support for fields annotated with {@link io.github.anvil.annotations.List}.</p>
 */
public class GsonProcessor extends Processor<JsonElement> {
    private static final Logger logger = LoggerFactory.getLogger(GsonProcessor.class);

    /**
     * {@inheritDoc}
     */
    @Override
    public Boolean getBooleanFieldValue(JsonElement input, String fieldName) {
        return input.getAsJsonObject().get(fieldName).getAsBoolean();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Number getNumberFieldValue(JsonElement input, Class<?> numberClass, String fieldName) {
        JsonElement element = input.getAsJsonObject().get(fieldName);

        return switch (numberClass.getSimpleName()) {
            case "Integer", "int" -> element.getAsInt();
            case "Long", "long" -> element.getAsLong();
            case "Float", "float" -> element.getAsFloat();
            case "Double", "double" -> element.getAsDouble();
            case "Short", "short" -> element.getAsShort();
            case "Byte", "byte" -> element.getAsByte();
            default -> throw new IllegalArgumentException("Unsupported number class: " + numberClass.getName() + ".");
        };
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getStringFieldValue(JsonElement input, String fieldName) {
        return input.getAsJsonObject().get(fieldName).getAsString();
    }

    /**
     * Extracts a nested input representation for a field marked as an inner schema or list.
     *
     * <p>This method extracts the field value from the parent {@link JsonObject}. If the field
     * contains a JSON array, it returns the {@link com.google.gson.JsonArray} directly, which allows
     * the {@link io.github.anvil.validation.validators.ListValidator} to process list fields
     * annotated with {@link io.github.anvil.annotations.List}. For nested objects, it returns the
     * {@link JsonElement} representing the nested structure.</p>
     *
     * @param input     the input source (must be a {@link JsonObject}).
     * @param fieldName the name of the field to read.
     * @return the nested input representation (JsonArray for arrays, JsonElement for objects),
     *         or {@code null} if the field is not present.
     */
    @Override
    public JsonElement getInnerInput(JsonElement input, String fieldName) {
        JsonElement jsonElement = input.getAsJsonObject().get(fieldName);

        if (jsonElement instanceof JsonArray jsonArray) {
            return jsonArray;
        }

        return jsonElement;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Logger getLogger() {
        return logger;
    }
}
