# Core Concepts

This page explains how Anvil models validation: schemas, processors, annotations, and how the validation pipeline works.

## Schemas and the `@Validate` annotation

At the heart of Anvil is the `Schema` base class. Any class you want to validate must:

- Extend `Schema`
- Be annotated with `@Validate`

```java
import io.github.anvil.Schema;
import io.github.anvil.annotations.Validate;
import io.github.anvil.annotations.ValidateField;

@Validate
public class User extends Schema {
    @ValidateField
    private String username;

    @ValidateField
    private String email;
}
```

The `@Validate` annotation controls:

- `value` - Enables or disables validation for the class (default: `true`).
- `printInfo` - When `true`, Anvil logs validation metadata for the class.
- `failFast` - When `true`, validation stops on the first error.

Every field in a validated schema **must** be annotated with `@ValidateField`. If a field is missing this annotation,
Anvil fails fast at startup with an error.

## The validation pipeline

At runtime, you create an `Anvil<IN>` instance, passing a concrete processor implementation (Gson or Jackson).
You then call `validate(input, YourSchema.class)`:

1. **Schema introspection**
    - Anvil checks that the class is annotated with `@Validate`.
    - If `value = false`, validation is disabled and an error is thrown.
    - It verifies that every field has `@ValidateField` and that annotation combinations are allowed.

2. **Input extraction**
    - For each field, the processor reads the value from the input JSON (boolean, number, string, etc.).
    - Missing fields are treated as `null`.

3. **Annotation-based validation**
    - For each field, Anvil collects all annotations (e.g. `@Regex`, `@Between`, `@StrIn`).
    - It looks up a `Validator` implementation for each annotation and runs them in order.
    - Validators can transform the value (e.g. trimming) or throw `ValidationError` on failure.

4. **Error handling and `failFast`**
    - If `failFast = true`, the first `ValidationError` stops processing and is thrown immediately.
    - Otherwise, errors are accumulated and wrapped into a single `ValidationException` at the end.

5. **Object construction**
    - If there are no errors, Anvil constructs the schema instance, assigns all validated field values, and returns it.

Under the hood, `Anvil` delegates to the processor and either returns a fully built `User` or throws
`ValidationException` with all collected `ValidationError` instances.

## Optional vs required fields

By default, fields are required:

```java

@ValidateField  // required = true by default
private String email;
```

You can make a field optional by setting `required = false`:

```java

@ValidateField(required = false)
private String nickname;
```

If an optional field is **missing** from the input:

- Anvil does not add a validation error.
- The field is set to `null` (or left with the default value, depending on your type).

If the optional field **is present**:

- All attached validators are applied.
- Any failing constraint still produces a `ValidationError`.

## Pre- and post-build hooks

Schemas can participate in the validation lifecycle by overriding two methods on `Schema`:

- `preBuild()` - Called **before** field values are assigned.
- `postBuild()` - Called **after** field values are assigned.

Both methods can throw `ValidationError`, which is treated like any other validation failure.

```java

@Validate
public class User extends Schema {
    @ValidateField
    private String username;

    @Override
    public void preBuild() throws ValidationError {
        // Runs before JSON values are applied
    }

    @Override
    public void postBuild() throws ValidationError {
        if (username != null && username.length() < 3) {
            throw new ValidationError("Username must be at least 3 characters long");
        }
    }
}
```

Use these hooks for cross-field checks or business rules that cannot be expressed with a single-field annotation.

## Error model

Anvil distinguishes between two error types:

- `ValidationError` - A single validation problem (e.g. “age must be >= 18”). Thrown by validators or lifecycle hooks.
- `ValidationException` - A runtime exception thrown by `Anvil.validate(...)` that aggregates one or more
  `ValidationError` instances into a human-readable message.

With `failFast = false` (the default), you typically see all validation errors in one go,
which can be especially useful for APIs and form validation.
