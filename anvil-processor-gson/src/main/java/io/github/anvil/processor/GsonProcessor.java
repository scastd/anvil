package io.github.anvil.processor;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * {@link io.github.anvil.processor.Processor} implementation for Gson's {@link JsonObject}.
 *
 * <p>This processor knows how to extract booleans, numbers, and strings from a {@link JsonObject}
 * so that Anvil can validate and build schema instances from Gson-based JSON inputs.</p>
 */
public class GsonProcessor extends Processor<JsonObject> {
    private static final Logger logger = LoggerFactory.getLogger(GsonProcessor.class);

    /**
     * {@inheritDoc}
     */
    @Override
    public Boolean getBooleanFieldValue(JsonObject input, String fieldName) {
        return input.get(fieldName).getAsBoolean();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Number getNumberFieldValue(JsonObject input, Class<?> numberClass, String fieldName) {
        JsonElement element = input.get(fieldName);

        return switch (numberClass.getSimpleName()) {
            case "Integer", "int" -> element.getAsInt();
            case "Long", "long" -> element.getAsLong();
            case "Float", "float" -> element.getAsFloat();
            case "Double", "double" -> element.getAsDouble();
            case "Short", "short" -> element.getAsShort();
            case "Byte", "byte" -> element.getAsByte();
            default -> throw new IllegalArgumentException("Unsupported number class: " + numberClass.getName());
        };
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getStringFieldValue(JsonObject input, String fieldName) {
        return input.get(fieldName).getAsString();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Logger getLogger() {
        return logger;
    }
}
