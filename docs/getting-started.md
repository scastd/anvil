# Getting Started

This guide walks you through installing Anvil, defining your first schema, and validating JSON using Gson or Jackson.

## Prerequisites

- **Java**: 21 or higher.
- **Build tool**: Maven or Gradle.
- **JSON library**: Gson or Jackson (or both).

## 1. Install the dependencies

Add the core library and at least one processor to your project.
See the **Installation** section on the [Home](index.md#installation) page for full Maven and Gradle snippets.

At minimum, you need:

- `anvil-core`
- `anvil-processor-gson` **or** `anvil-processor-jackson` (or both, if you need)

## 2. Define a schema

Create a class that extends `Schema` and annotate it with `@Validate`. Use `@ValidateField` and other annotations
to describe the validation rules for each field.

```java
import io.github.anvil.Schema;
import io.github.anvil.annotations.Validate;
import io.github.anvil.annotations.ValidateField;
import io.github.anvil.annotations.StrIn;
import io.github.anvil.annotations.numeric.Between;

@Validate
public class User extends Schema {
    @ValidateField
    @StrIn({ "admin", "user", "guest" })
    private String role;

    @ValidateField
    @Between(min = 18, max = 120)
    private int age;

    @ValidateField
    private String email;

    @ValidateField(required = false)
    private String nickname;
}
```

## 3. Validate JSON

Create an `Anvil` instance with your preferred JSON processor, then call `validate`.

=== "`gson`"

    ```java
    import com.google.gson.JsonObject;
    import io.github.anvil.Anvil;
    import io.github.anvil.exceptions.ValidationException;
    import io.github.anvil.processor.GsonProcessor;

    public class Main {
        public static void main(String[] args) {
            Anvil<JsonObject> anvil = new Anvil<>(new GsonProcessor());

            JsonObject json = new JsonObject();
            json.addProperty("role", "admin");
            json.addProperty("age", 25);
            json.addProperty("email", "user@example.com");

            try {
                User user = anvil.validate(json, User.class);
                System.out.println("Validation successful!");
            } catch (ValidationException e) {
                System.out.println("Validation failed: " + e.getMessage());
            }
        }
    }
    ```

=== "`jackson`"

    ```java
    import com.fasterxml.jackson.databind.ObjectMapper;
    import com.fasterxml.jackson.databind.node.ObjectNode;
    import io.github.anvil.Anvil;
    import io.github.anvil.exceptions.ValidationException;
    import io.github.anvil.processor.JacksonProcessor;

    public class Main {
        public static void main(String[] args) throws Exception {
            ObjectMapper mapper = new ObjectMapper();
            ObjectNode json = mapper.createObjectNode()
                .put("role", "admin")
                .put("age", 25)
                .put("email", "user@example.com");

            Anvil<ObjectNode> anvil = new Anvil<>(new JacksonProcessor());

            try {
                User user = anvil.validate(json, User.class);
                System.out.println("Validation successful!");
            } catch (ValidationException e) {
                System.out.println("Validation failed: " + e.getMessage());
            }
        }
    }
    ```
