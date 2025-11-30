package io.github.anvil;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class TestUtils {
    public static ObjectMapper objectMapper = new ObjectMapper();

    public static ObjectNode getObjectNode(String json) {
        try {
            return objectMapper.readValue(json, ObjectNode.class);
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse JSON: " + json, e);
        }
    }
}
