# Annotations

This page lists the core annotations that can be used when defining schemas with Anvil.

## Schema annotations

### `@Validate`

Marks a class as a validated schema.

```java
import io.github.anvil.Schema;
import io.github.anvil.annotations.Validate;

@Validate
public class User implements Schema {
    // fields...
}
```

[//]: # (@formatter:off)
!!! warning
    The class must extend `Schema`. If it does not, your project will not compile.

[//]: # (@formatter:on)

Options:

- `value` - Enable or disable validation for this class (default: `true`).
- `printInfo` - When `true`, logs validation metadata for the class (default: `false`).
- `failFast` - When `true`, stops validation on the first error (default: `false`).

```java

@Validate(
    value = true,
    printInfo = false,
    failFast = false
)
public class User implements Schema {
    // fields...
}
```

## Field annotations

### `@OptionalValue`

Marks a field as **optional** during validation.

By default, all fields in a `@Validate`d schema are required: if the input is missing a value for a field, Anvil
produces a validation error. Annotating a field with `@OptionalValue` allows the field to be omitted without error.

```java
public class User implements Schema {
    private String email; // required by default

    @OptionalValue
    private String nickname; // optional
}
```

## String annotations

All string annotations live under `io.github.anvil.annotations` and are applied to `String` fields.

### `@Regex`

Validates that a string matches a regular expression.

```java

@Regex("^[a-zA-Z0-9_]{3,16}$")
private String username;
```

Options:

- `value` - Regular expression pattern to match against the field value.

### `@StrEqual`

Validates that a string exactly matches a given value (case-sensitive by default).

```java

@StrEqual("ACTIVE")
private String status;
```

Options:

- `value` - String value the field must equal.
- `strategy` - Comparison strategy, using `StringComparisonStrategy` (`CASE_SENSITIVE` by default).

### `@StrIn`

Validates that a string is one of a set of allowed values.

```java

@StrIn({"admin", "user", "guest"})
private String role;
```

Options:

- `value` - Array of allowed string values.
- `strategy` - Comparison strategy, using `StringComparisonStrategy` (`CASE_SENSITIVE` by default).

## Enum annotations

### `@EnumValue`

Validates that a field’s value is one of the constants of a given enum class.

```java
public enum Role {
    ADMIN,
    USER,
    GUEST
}

public class User implements Schema {
    @EnumValue(Role.class)
    private Role role;
}
```

Options:

- `value` - Enum class that defines the allowed constants.

## Numeric annotations

All numeric annotations live in `io.github.anvil.annotations.numeric` and work with common numeric types
(`int`, `long`, `float`, `double` and their wrappers). You can use either integer or floating-point literals;
Anvil will cast them as needed.

### `@Equal`

Validates that a numeric field equals a specific value.

```java

@Equal(42.0)
private double answer;
```

Options:

- `value` - Numeric value the field must equal.

### `@Between`

Validates that a numeric field is between two values \[min, max).

```java

@Between(min = 0.0f, max = 100.0f)
private float percentage;
```

Options:

- `min` - Inclusive lower bound for the field value.
- `max` - Exclusive upper bound for the field value.

### `@Greater`

Validates that a numeric field is strictly greater than a value.

```java

@Greater(0.0f)
private float positiveNumber;
```

Options:

- `value` - Threshold that the field value must be strictly greater than.

### `@GreaterOrEqual`

Validates that a numeric field is greater than or equal to a value.

```java

@GreaterOrEqual(18.0f)
private float age;
```

Options:

- `value` - Threshold that the field value must be greater than or equal to.

### `@Less`

Validates that a numeric field is strictly less than a value.

```java

@Less(100.0f)
private float maxScore;
```

Options:

- `value` - Upper bound that the field value must be strictly less than.

### `@LessOrEqual`

Validates that a numeric field is less than or equal to a value.

```java

@LessOrEqual(10.0f)
private float rating;
```

Options:

- `value` - Upper bound that the field value must be less than or equal to.

### `@In`

Validates that a numeric field is one of a discrete set of values.

```java

@In({1.0, 2.0, 3.0})
private double level;
```

Options:

- `value` - Array of allowed numeric values for the field.
