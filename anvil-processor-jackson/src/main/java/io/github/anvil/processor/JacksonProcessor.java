package io.github.anvil.processor;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * {@link io.github.anvil.processor.Processor} implementation for Jackson's {@link ObjectNode}.
 *
 * <p>This processor knows how to extract booleans, numbers, and strings from an {@link ObjectNode}
 * so that Anvil can validate and build schema instances from Jackson-based JSON inputs.</p>
 */
public class JacksonProcessor extends Processor<ObjectNode> {
    private static final Logger logger = LoggerFactory.getLogger(JacksonProcessor.class);

    /**
     * {@inheritDoc}
     */
    @Override
    public Boolean getBooleanFieldValue(ObjectNode input, String fieldName) {
        return input.get(fieldName).asBoolean();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Number getNumberFieldValue(ObjectNode input, Class<?> numberClass, String fieldName) {
        JsonNode element = input.get(fieldName);

        return switch (numberClass.getSimpleName()) {
            case "Integer", "int" -> element.intValue();
            case "Long", "long" -> element.longValue();
            case "Float", "float" -> element.floatValue();
            case "Double", "double" -> element.doubleValue();
            case "Short", "short" -> element.shortValue();
            case "Byte", "byte" -> element.numberValue().byteValue();
            default -> throw new IllegalArgumentException("Unsupported number class: " + numberClass.getName());
        };
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getStringFieldValue(ObjectNode input, String fieldName) {
        return input.get(fieldName).asText();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Logger getLogger() {
        return logger;
    }
}
