# AnViL - Annotation Validation Library

**Anvil** is a powerful and type-safe Java validation library for JSON deserialization with annotation-based schema
validation. It provides declarative validation rules that ensure your data meets the requirements before it is
processed.

## Features

✨ **Declarative Validation** - Define validation rules using simple annotations.

🎯 **Type-Safe** - Full compile-time type checking with Java generics.

🔌 **Multiple JSON Processors** - Built-in support for Gson and Jackson.

📝 **Rich Annotation Set** - Comprehensive validation annotations for all common scenarios.

⚡️ **Fail-Fast Mode** - Stop validation on first error.

🎨 **Extensible** - Easy to add custom validators and processors.

🔍 **Detailed Error Messages** - Clear validation errors with field-level granularity.

## Why Anvil?

- **Declarative Validation**: Define rules with annotations instead of imperative code.
- **Framework Agnostic**: Works with Gson, Jackson, or implement your own processor.
- **Rich Annotations**: Numeric comparisons, string matching, enums, and custom validators.
- **Developer Friendly**: Clear error messages and fail-fast validation.

## Modules

Anvil is split into three modules:

- **anvil-core** - Core annotations and validation framework.
- **anvil-processor-gson** - Gson processor implementation.
- **anvil-processor-jackson** - Jackson processor implementation.

Choose the processor that matches your JSON library!

## Installation

### `anvil-core`

=== "Maven"

    ```xml
    <dependency>
        <groupId>io.github.scastd</groupId>
        <artifactId>anvil-core</artifactId>
        <version>0.1.1</version>
    </dependency>
    ```

=== "Gradle"

    ```groovy
    implementation 'io.github.scastd:anvil-core:0.1.1'
    ```

### `anvil-processor-gson`

=== "Maven"

    ```xml
    <dependency>
        <groupId>io.github.scastd</groupId>
        <artifactId>anvil-processor-gson</artifactId>
        <version>0.1.1</version>
    </dependency>
    ```

=== "Gradle"

    ```groovy
    implementation 'io.github.scastd:anvil-processor-gson:0.1.1'
    ```

### `anvil-processor-jackson`

=== "Maven"

    ```xml
    <dependency>
        <groupId>io.github.scastd</groupId>
        <artifactId>anvil-processor-jackson</artifactId>
        <version>0.1.1</version>
    </dependency>
    ```

=== "Gradle"

    ```groovy
    implementation 'io.github.scastd:anvil-processor-jackson:0.1.1'
    ```

### Requirements

- Java 21 or higher

## Quick Start

```java title="User.java"

@Validate
public class User implements Schema {
    private String username;

    @Between(min = 18, max = 120)
    private Integer age;

    @In({"admin", "user", "guest"})
    private String role;
}
```

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

[//]: # (@formatter:off)

!!! note
    This is a simplified example. See the [Getting Started](getting-started.md) guide for full details, including processor configuration and validation workflow.

[//]: # (@formatter:on)

## Documentation

The full Javadocs are available [here](https://javadoc.io/doc/io.github.scastd/anvil-core/latest/index.html).

[//]: # (@formatter:off)

!!! tip
    If you want to see the Javadoc for any of the processor modules, just change `anvil-core` in the top bar to the
    desired module name, e.g., `anvil-processor-gson` or `anvil-processor-jackson`.

[//]: # (@formatter:on)
