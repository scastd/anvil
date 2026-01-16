# Processors

Anvil uses *processors* to read JSON input and feed values into your validated schemas.

You typically do **not** implement validation logic in a processor. Instead, you:

- Choose a processor for your JSON library (Gson or Jackson).
- Pass it to `Anvil<IN>`.
- Let Anvil and the validators handle the schema rules.

=== "`gson`"

    ```java
    Anvil<JsonObject> anvil = new Anvil<>(new GsonProcessor());
    User user = anvil.validate(jsonObject, User.class);
    ```

=== "`jackson`"

    ```java
    Anvil<ObjectNode> anvil = new Anvil<>(new JacksonProcessor());
    User user = anvil.validate(objectNode, User.class);
    ```

## Built-in processors

### `GsonProcessor`

Located in the `anvil-processor-gson` module as `io.github.anvil.processor.GsonProcessor`.

```java
import com.google.gson.JsonObject;
import io.github.anvil.Anvil;
import io.github.anvil.processor.GsonProcessor;

Anvil<JsonObject> anvil = new Anvil<>(new GsonProcessor());
User user = anvil.validate(jsonObject, User.class);
```

- Accepts `JsonObject` as the input type.
- Uses Gson APIs to extract booleans, numbers and strings by field name.
- Logs validation information through SLF4J.

### `JacksonProcessor`

Located in the `anvil-processor-jackson` module as `io.github.anvil.processor.JacksonProcessor`.

```java
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.github.anvil.Anvil;
import io.github.anvil.processor.JacksonProcessor;

ObjectMapper mapper = new ObjectMapper();
ObjectNode json = mapper.createObjectNode()
    .put("role", "admin")
    .put("age", 25)
    .put("email", "user@example.com");

Anvil<ObjectNode> anvil = new Anvil<>(new JacksonProcessor());
User user = anvil.validate(json, User.class);
```

- Accepts `ObjectNode` as the input type.
- Uses Jackson APIs to extract booleans, numbers and strings by field name.
- Logs validation information through SLF4J.

## Custom processors

If you use a different JSON representation (or want to plug Anvil into another data source), you can create your own processor by extending `Processor<IN>`.

```java title="MapProcessor.java"
import io.github.anvil.Anvil;
import io.github.anvil.Schema;
import io.github.anvil.processor.Processor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public class MapProcessor extends Processor<Map<String, Object>> {
    private static final Logger logger = LoggerFactory.getLogger(MapProcessor.class);

    @Override
    public Boolean getBooleanFieldValue(Map<String, Object> input, String fieldName) {
        return (Boolean) input.get(fieldName);
    }

    @Override
    public Number getNumberFieldValue(Map<String, Object> input, Class<?> numberClass, String fieldName) {
        return (Number) input.get(fieldName);
    }

    @Override
    public String getStringFieldValue(Map<String, Object> input, String fieldName) {
        Object value = input.get(fieldName);
        return value != null ? value.toString() : null;
    }

    @Override
    public Map<String, Object> getInnerInput(Map<String, Object> input, String fieldName) {
        Object value = input.get(fieldName);
        return value instanceof Map ? (Map<String, Object>) value : null;
    }

    @Override
    public Logger getLogger() {
        return logger;
    }
}
```

Usage:

```java
Anvil<Map<String, Object>> anvil = new Anvil<>(new MapProcessor());
User user = anvil.validate(inputMap, User.class);
```

When implementing a custom processor you need to:

- Choose the input type `IN` (e.g. `JsonNode`, `Map<String, Object>`, a custom DTO).
- Implement how to read booleans, numbers and strings by field name.
- Implement `getInnerInput` to extract nested objects for fields annotated with `@Inner`.
- Provide a `Logger` instance.
