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
    The class must implement `Schema`. If it does not, your project will not compile.

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

By default, all fields in a schema are **required**: if the input is missing a value for a field, Anvil
produces a validation error. Annotating a field with `@OptionalValue` allows the field to be omitted without error.

```java
public class User implements Schema {
    private String email; // required by default

    @OptionalValue
    private String nickname; // optional
}
```

### `@Inner`

Marks a field as containing a nested schema that should be validated independently.

Options:

- `value` - The schema class to use for validating the nested object. Must be a class annotated with `@Validate`
  that implements `Schema`.

When a field is annotated with `@Inner`, the field's value is extracted from the input as a nested object and validated
using the schema class specified by the annotation's `value`. The nested validation follows the same rules as
top-level schema validation, including all field annotations and validation rules defined in the nested schema class.

```java

@Validate
public class Address implements Schema {
    private String street;
    private String city;
}

@Validate
public class User implements Schema {
    private String name;

    @Inner(Address.class)
    private Address address;
}
```

#### Recursive processing

Anvil processes nested schemas recursively. When validating a field annotated with `@Inner`:

1. The nested input object is extracted from the parent input.
2. The nested schema is validated independently using the same validation pipeline.
3. All validation rules (annotations, `postBuild()` hooks) are applied to the nested schema.
4. If validation succeeds, the validated nested schema instance is assigned to the field.
5. If validation fails, errors are collected and prefixed with the field path.

#### Error path building

When nested validation fails, error messages are automatically prefixed with the full field path from the root element.
This makes it clear which nested field has the error.

For example, with the following structure:

```java

@Validate
public class Country implements Schema {
    @StrEqual("USA")
    private String code;
}

@Validate
public class Address implements Schema {
    @Inner(Country.class)
    private Country country;
}

@Validate
public class User implements Schema {
    @Inner(Address.class)
    private Address address;
}
```

If the `code` field in `Country` fails validation, the error message will show the full path:

```
for field 'address.country.code': Found value 'CAN', but expected equal to: 'USA'.
```

This path building works recursively for any depth of nesting, ensuring that errors always show the complete path
from the root element to the field with the error.

#### Fail-fast behavior

The `failFast` setting on `@Validate` applies independently to each schema level. If a nested schema has
`failFast = true`, validation stops on the first error within that nested schema, but the parent schema's
`failFast` setting determines whether other fields at the parent level continue to be validated.

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

### `@UUID`

Validates that a string is a valid UUID format and transforms it into a `UUID` object.

```java
import java.util.UUID;

@UUID
private UUID userId;
```

The validator checks that the string matches the standard UUID format with hyphens separating the five groups of
hexadecimal digits (8-4-4-4-12). If valid, the string is converted to a `java.util.UUID` object and assigned to the field.

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
