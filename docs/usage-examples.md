# Usage Examples

This page shows concrete scenarios where Anvil can help you validate JSON payloads.

## User registration

In this example, we validate a typical user registration payload.

```java title="UserRegistration.java"
@Validate
public class UserRegistration implements Schema {
    @ValidateField
    private String username;

    @ValidateField
    @Regex("^.{8,128}$")  // Password must be between 8 and 128 characters
    private String password;

    @ValidateField
    @GreaterOrEqual(18)
    private int age;

    @ValidateField
    @StrIn({ "US", "CA", "GB", "AU" })
    private String countryCode;

    @ValidateField(required = false)
    private String phoneNumber;

    @Override
    public void postBuild() throws ValidationError {
        if (username.length() < 3) {
            throw new ValidationError("Username must be at least 3 characters");
        }
    }
}
```

=== "`gson`"

    ```java
    JsonObject json = new JsonObject();
    json.addProperty("username", "sam");
    json.addProperty("password", "super-secure-password");
    json.addProperty("age", 25);
    json.addProperty("countryCode", "US");

    Anvil<JsonObject> anvil = new Anvil<>(new GsonProcessor());
    UserRegistration user = anvil.validate(json, UserRegistration.class);
    ```

=== "`jackson`"

    ```java
    ObjectMapper mapper = new ObjectMapper();
    ObjectNode json = mapper.createObjectNode()
        .put("username", "sam")
        .put("password", "super-secure-password")
        .put("age", 25)
        .put("countryCode", "US");

    Anvil<ObjectNode> anvil = new Anvil<>(new JacksonProcessor());
    UserRegistration user = anvil.validate(json, UserRegistration.class);
    ```

## API configuration

Here we validate configuration for an API server.

```java
@Validate(failFast = true)
public class ApiConfig implements Schema {
    @ValidateField
    @StrIn({ "development", "staging", "production" })
    private String environment;

    @ValidateField
    @Between(min = 1000, max = 65535)
    private int port;

    @ValidateField
    @GreaterOrEqual(1)
    private int maxConnections;

    @ValidateField(required = false)
    @Between(min = 1, max = 3600)
    private Integer timeoutSeconds;
}
```

Usage:

```java
ApiConfig config = anvil.validate(jsonConfig, ApiConfig.class);
server.start(config.getPort(), config.getMaxConnections());
```

## 🏗️ Advanced composition

Currently, Anvil validates flat schemas only. If you need to validate nested data structures, you can:

- Flatten the payload into a single schema.
- Run validation in multiple passes with separate schemas and compose the results in your own code.
