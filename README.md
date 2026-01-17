<h2 align="center">
  <img src="docs/assets/logo_128.png" alt="Anvil">
  <br>
  <strong>AnViL</strong>
</h2>

A powerful and type-safe Java **A**nnotation **V**alidation **L**ibrary for JSON deserialization with annotation-based
schema validation. It provides declarative validation rules that ensure your data meets the requirements before
it is processed.

<div align="center">

[![Main pipeline](https://github.com/scastd/anvil/actions/workflows/1.pipeline.yml/badge.svg)](https://github.com/scastd/anvil/actions/workflows/1.pipeline.yml)
[![License](https://img.shields.io/badge/License-Apache%202.0-blue?logo=apache)](LICENSE)
[![Java 21](https://img.shields.io/badge/Java-21-orange)]()
<br>
[![Maven Central](https://img.shields.io/badge/Maven%20Central-anvil--core-b949cd?logo=apachemaven)](https://central.sonatype.com/artifact/io.github.scastd/anvil-core)
[![Maven Central](https://img.shields.io/badge/Maven%20Central-anvil--processor--gson-b949cd?logo=apachemaven)](https://central.sonatype.com/artifact/io.github.scastd/anvil-processor-gson)
[![Maven Central](https://img.shields.io/badge/Maven%20Central-anvil--processor--jackson-b949cd?logo=apachemaven)](https://central.sonatype.com/artifact/io.github.scastd/anvil-processor-jackson)

</div>

## Table of Contents

- [Features](#features)
- [Installation](#installation)
- [Quick Start](#quick-start)
- [Annotations](#annotations)
    - [Schema Annotations](#schema-annotations)
    - [Field Annotations](#field-annotations)
    - [String Annotations](#string-annotations)
    - [Numeric Annotations](#numeric-annotations)
- [Usage Examples](#usage-examples)
- [Processors](#processors)
- [Advanced Features](#advanced-features)
- [Contributing](#contributing)
- [License](#license)

## Features

✨ **Declarative Validation** - Define validation rules using simple annotations.

🎯 **Type-Safe** - Full compile-time type checking with Java generics.

🔌 **Multiple JSON Processors** - Built-in support for Gson and Jackson.

📝 **Rich Annotation Set** - Comprehensive validation annotations for all common scenarios.

⚡ **Fail-Fast Mode** - Stop validation on first error.

🎨 **Extensible** - Easy to add custom validators and processors.

🔍 **Detailed Error Messages** - Clear validation errors with field-level granularity.

## Installation

### Maven

Add the following dependencies to your `pom.xml`:

- Core library

```xml

<dependency>
    <groupId>io.github.scastd</groupId>
    <artifactId>anvil-core</artifactId>
    <version>0.1.0</version>
</dependency>
```

- For Gson support

```xml

<dependency>
    <groupId>io.github.scastd</groupId>
    <artifactId>anvil-processor-gson</artifactId>
    <version>0.1.0</version>
</dependency>
```

- For Jackson support

```xml

<dependency>
    <groupId>io.github.scastd</groupId>
    <artifactId>anvil-processor-jackson</artifactId>
    <version>0.1.0</version>
</dependency>
```

### Gradle

Add the following dependencies to your `build.gradle`:

- Core library

```groovy
implementation 'io.github.scastd:anvil-core:0.1.0'
```

- For Gson support

```groovy
implementation 'io.github.scastd:anvil-processor-gson:0.1.0'
```

- For Jackson support

```groovy
implementation 'io.github.scastd:anvil-processor-jackson:0.1.0'
```

### Requirements

- Java 21 or higher
- Maven 3.6+ (for building from source)

## Quick Start

Here's a simple example using Gson:

```java
import com.google.gson.JsonObject;
import io.github.anvil.Anvil;
import io.github.anvil.Schema;
import io.github.anvil.annotations.StrIn;
import io.github.anvil.annotations.Validate;
import io.github.anvil.annotations.numeric.Between;
import io.github.anvil.exceptions.ValidationException;
import io.github.anvil.processor.GsonProcessor;

public class Main {
    @Validate
    public static class User implements Schema {
        @StrIn({"admin", "user", "guest"})
        String role;

        @Between(min = 18, max = 120)
        int age;

        String email;

        @OptionalValue
        String nickname;
    }

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

## Annotations

Anvil provides a variety of annotations to define validation rules:

### Schema Annotations

- @Validate — Marks a class as eligible for validation.

### Field Annotations

- @Inner — Marks a field as a nested schema to be validated.
- @List — Marks a field as a list of schema elements to be validated.
- @OptionalValue — Marks a field as optional during validation.
- @EnumValue — Validates that a field value is a valid constant of the specified Enum type.

### String Annotations

- @StrIn — Validates that a String field matches one of a predefined set of values.
- @StrEqual — Validates that a String field equals a specific value.
- @Regex — Validates that a String field matches a given regular expression.
- @UUID — Validates that a string value is a valid UUID format and transforms it into a UUID object.

### Numeric Annotations

- @Between — Validates that a numeric field is within a specified range.
- @Equal — Validates that a numeric field is exactly equal to a configured value.
- @GreaterOrEqual — Validates that a numeric field is greater than or equal to a configured value.
- @Greater — Validates that a numeric field is strictly greater than a configured value.
- @LessOrEqual — Validates that a numeric field is less than or equal to a configured value.
- @Less — Validates that a numeric field is strictly less than a configured value.
- @In — Validates that a numeric field is contained in a configured set of values.

## Usage Examples

For complete, up-to-date examples, see the documentation:

- User registration - [docs](https://scastd.github.io/anvil/usage-examples/#user-registration)
- API configuration - [docs](https://scastd.github.io/anvil/usage-examples/#api-configuration)
- Gson/Jackson quickstart - [docs](https://scastd.github.io/anvil/usage-examples/)

## Processors

Anvil supports multiple JSON processing libraries through processor implementations:

### GsonProcessor

For use with Google's Gson library.

```java
import com.google.gson.JsonObject;
import io.github.anvil.processor.GsonProcessor;

Anvil<JsonObject> anvil = new Anvil<>(new GsonProcessor());
```

### JacksonProcessor

For use with FasterXML's Jackson library.

```java
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.github.anvil.processor.JacksonProcessor;

Anvil<ObjectNode> anvil = new Anvil<>(new JacksonProcessor());
```

### Custom Processor

You can implement your own processor by extending the `Processor<IN>` abstract class:

```java
public class CustomProcessor extends Processor<YourInputJsonType> {
    @Override
    public <T extends Schema> T process(
        YourInputJsonType input,
        Class<T> clazz
    ) {
        // Your implementation
    }
}
```

## Advanced Features

### Post-build Hook

Override `postBuild()` in your schema for custom logic that runs after field values are assigned:

```java

@Validate
public class User implements Schema {
    String password;

    @Override
    public void postBuild() throws ValidationError {
        if (role.equalsIgnoreCase("administrator")) {
            throw new ValidationError("for field 'role': Role 'administrator' is not allowed.");
        }

        System.out.println("Deserialization completed.");
    }
}
```

### Fail-Fast Mode

The `failFast` option in `@Validate` stops validation on the first error when set to `true`:

```java

@Validate(failFast = true)
public class MyClass implements Schema {
    // Validation will stop at first error
}
```

### Debug Mode

Enable debug mode to print validation information before the processing starts:

```java

@Validate(printInfo = true)
public class MyClass implements Schema {
    // Will print validation info during processing
}
```

## Architecture

Anvil follows a clean, modular architecture:

```
anvil-core       -> Core validation framework and annotations
├── Anvil        -> Main validation orchestrator
├── Schema       -> Base interface for validated objects
├── annotations  -> Validation annotations
└── validation   -> Validator implementations

anvil-processor-gson  -> Gson integration
└── GsonProcessor     -> Gson-specific implementation

anvil-processor-jackson  -> Jackson integration
└── JacksonProcessor     -> Jackson-specific implementation
```

## Contributing

Contributions are welcome! Please read our [Contributing Guide](CONTRIBUTING.md) for details on:

- Code of conduct
- Development process
- Submitting pull requests
- Reporting bugs
- Suggesting enhancements

## License

This project is licensed under the Apache License 2.0 - see the [LICENSE](LICENSE) file for details.

## Support

- **Issues**: [GitHub Issues](https://github.com/scastd/anvil/issues)
- **Email**: scastd00@gmail.com
- **Documentation**: Check the extensive docs [here](https://scastd.github.io/anvil/).

## Acknowledgments

Built with ❤️ by [Samuel Castrillo](https://github.com/scastd)
