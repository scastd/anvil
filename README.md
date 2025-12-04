<h2 align="center">
  <img src="docs/assets/logo_128.png" alt="Anvil">
  <br>
  <strong>AnViL</strong>
</h2>

A powerful and type-safe Java **A**nnotation **V**alidation **L**ibrary for JSON deserialization with annotation-based schema validation. It
provides declarative validation rules that ensure your data meets the requirements before it is processed.

[![Main pipeline](https://github.com/scastd/anvil/actions/workflows/1.pipeline.yml/badge.svg)](https://github.com/scastd/anvil/actions/workflows/1.pipeline.yml)
[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](LICENSE)
[![Java 21](https://img.shields.io/badge/Java-21-orange.svg)](https://www.oracle.com/java/)
[![Maven Central](https://img.shields.io/maven-central/v/io.github/anvil.svg)](https://central.sonatype.com/artifact/io.github/anvil)

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
    <version>0.0.6</version>
</dependency>
```

- For Gson support

```xml
<dependency>
    <groupId>io.github.scastd</groupId>
    <artifactId>anvil-processor-gson</artifactId>
    <version>0.0.6</version>
</dependency>
```

- For Jackson support

```xml
<dependency>
    <groupId>io.github.scastd</groupId>
    <artifactId>anvil-processor-jackson</artifactId>
    <version>0.0.6</version>
</dependency>
```

### Gradle

Add the following dependencies to your `build.gradle`:

- Core library

```groovy
implementation 'io.github.scastd:anvil-core:0.0.6'
```

- For Gson support

```groovy
implementation 'io.github.scastd:anvil-processor-gson:0.0.6'
```

- For Jackson support

```groovy
implementation 'io.github.scastd:anvil-processor-jackson:0.0.6'
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
import io.github.anvil.annotations.ValidateField;
import io.github.anvil.annotations.numeric.Between;
import io.github.anvil.exceptions.ValidationException;
import io.github.anvil.processor.GsonProcessor;

public class Main {
    @Validate
    public static class User implements Schema {
        @ValidateField
        @StrIn({ "admin", "user", "guest" })
        String role;

        @ValidateField
        @Between(min = 18, max = 120)
        int age;

        @ValidateField
        String email;

        @ValidateField(required = false)
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

### Schema Annotations

#### `@Validate`

Applied to a class to enable validation. The class **must** extend `Schema`.

```java
@Validate(
    value = true,       // Enable/disable validation (default: true)
    printInfo = false,  // Print validation info (default: false)
    failFast = false    // Stop on first error (default: false)
)
public class MyClass implements Schema {
    // fields...
}
```

### Field Annotations

#### `@ValidateField`

Applied to fields to specify validation rules. It must be used on all fields within a class annotated with `@Validate`.

```java
@ValidateField(
    required = true  // Field is required (default: true)
)
String myField;
```

#### `@EnumValue`

Specifies that a field's value must be one of the values defined in a given enum class.

```java
@ValidateField
@EnumValue(TheEnumClass.class)
TheEnumClass myEnumField;
```

### String Annotations

#### `@Regex`

Validates that a string field matches a given regular expression.

```java
@ValidateField
@Regex("^[a-zA-Z0-9_]{3,16}$")
String username;
```

#### `@StrEqual`

Validates that a string field equals a specific value (case-sensitive by default).

```java
@ValidateField
@StrEqual("ACTIVE")
String status;
```

#### `@StrIn`

Validates that a string field is one of the specified values (case-sensitive by default).

```java
@ValidateField
@StrIn({ "PENDING", "ACTIVE", "INACTIVE" })
String lifecycleState;
```

### Numeric Annotations

All numeric annotations live in `io.github.anvil.annotations.numeric` and support common numeric types (`int`, `long`,
`float`, `double`, and their wrappers). A float or int literal can be used depending on the field type (it is
automatically cast internally as needed).

#### `@Equal`

Validates that a numeric field equals a specific value.

```java
@ValidateField
@Equal(42.0)
double answer;
```

#### `@Between`

Validates that a numeric field is between two values \[min, max).

```java
@ValidateField
@Between(min = 0.0f, max = 100.0f)
float percentage;
```

#### `@Greater`

Validates that a numeric field is greater than a specific value.

```java
@ValidateField
@Greater(0.0f)
float positiveNumber;
```

#### `@GreaterOrEqual`

Validates that a numeric field is greater than or equal to a specific value.

```java
@ValidateField
@GreaterOrEqual(18.0f)
float age;
```

#### `@Less`

Validates that a numeric field is less than a specific value.

```java
@ValidateField
@Less(100.0f)
float maxScore;
```

#### `@LessOrEqual`

Validates that a numeric field is less than or equal to a specific value.

```java
@ValidateField
@LessOrEqual(10.0f)
float rating;
```

#### `@In`

Validates that a numeric field is one of the specified values.

```java
@ValidateField
@In({ 1.0, 2.0, 3.0 })
double level;
```

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
        Class<T> clazz,
        List<ValidationError> errors
    ) {
        // Your implementation
    }
}
```

## Advanced Features

### Pre- and Post-build Hooks

Override `preBuild()` and `postBuild()` methods in your schema for custom logic:

```java
@Validate
public class User implements Schema {
    @ValidateField
    String password;

    @Override
    public void preBuild() {
        System.out.println("Starting deserialization...");
    }

    @Override
    public void postBuild() throws ValidationError {
        if (role.equalsIgnoreCase("administrator")) {
            throw new ValidationError("Role 'administrator' is not allowed.");
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
├── Schema       -> Base class for validated objects
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
- **Documentation**: Check this README and extensive docs [here](https://scastd.github.io/anvil/).

## Acknowledgments

Built with ❤️ by [Samuel Castrillo](https://github.com/scastd)
