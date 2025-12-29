# Core Concepts

This page explains how Anvil models validation: schemas, processors, annotations, and how the validation pipeline works.

## Schemas and the `@Validate` annotation

At the heart of Anvil is the `Schema` base interface. Any class you want to validate must:

- Implement `Schema`
- Be annotated with `@Validate`

```java
import io.github.anvil.Schema;
import io.github.anvil.annotations.Validate;

@Validate
public class User implements Schema {
    private String username;

    private String email;
}
```

The `@Validate` annotation controls:

- `value` - Enables or disables validation for the class (default: `true`).
- `printInfo` - When `true`, Anvil logs validation metadata for the class.
- `failFast` - When `true`, validation stops on the first error.

By default, every field in a validated schema is **required**. If the input is missing a value for a field, Anvil
emits a validation error. You can mark a field as optional by annotating it with `@OptionalValue`.

## The validation pipeline

At runtime, you create an `Anvil<IN>` instance, passing a concrete processor implementation (Gson or Jackson).
You then call `validate(input, YourSchema.class)`:

1. **Schema introspection**
    - Anvil checks that the class is annotated with `@Validate`.
    - If `value = false`, validation is disabled and an error is thrown.
    - It verifies that annotation combinations are allowed (e.g. numeric range restrictions).

2. **Input extraction**
    - For each field, the processor reads the value from the input JSON (boolean, number, string, etc.).
    - Missing fields are treated as `null`.

3. **Annotation-based validation**
    - For each field, Anvil collects all annotations (e.g. `@Regex`, `@Between`, `@StrIn`).
    - It looks up a `Validator` implementation for each annotation and runs them in order.
    - Validators can transform the value (e.g. trimming) or throw `ValidationError` on failure.
   - For fields annotated with `@Inner`, the nested object is validated recursively using the same pipeline.
     Nested validation errors are automatically prefixed with the field path (e.g., `address.street`).

4. **Error handling and `failFast`**
    - If `failFast = true`, the first `ValidationError` stops processing and is thrown immediately.
    - Otherwise, errors are accumulated and wrapped into a single `ValidationException` at the end.
   - Nested validation errors preserve the full field path from the root element.

5. **Object construction**
    - If there are no errors, Anvil constructs the schema instance, assigns all validated field values, and returns it.
   - Nested schemas are constructed recursively before being assigned to their parent fields.

Under the hood, `Anvil` delegates to the processor and either returns a fully built `User` or throws
`ValidationException` with all collected `ValidationError` instances.

## Optional vs required fields

By default, fields are required:

```java
private String email;
```

You can make a field optional by annotating it with `@OptionalValue`:

```java

@OptionalValue
private String nickname;
```

If an optional field is **missing** from the input:

- Anvil does not add a validation error.
- The field is set to `null` (or left with the default value, depending on your type).

If the optional field **is present**:

- All attached validators are applied.
- Any failing constraint still produces a `ValidationError`.

## Post-build hook

Schemas can participate in the validation lifecycle by overriding the `postBuild()` method on `Schema`:

- `postBuild()` - Called **after** field values are assigned.

This method can throw `ValidationError`, which is treated like any other validation failure.

```java

@Validate
public class User implements Schema {
    private String username;

    @Override
    public void postBuild() throws ValidationError {
        if (username != null && username.length() < 3) {
            throw new ValidationError("Username must be at least 3 characters long");
        }
    }
}
```

Use this hook for cross-field checks or business rules that cannot be expressed with a single-field annotation.

## Error model

Anvil distinguishes between two error types:

- `ValidationError` - A single validation problem (e.g. “age must be >= 18”). Thrown by validators or lifecycle hooks.
- `ValidationException` - A runtime exception thrown by `Anvil.validate(...)` that aggregates one or more
  `ValidationError` instances into a human-readable message.

With `failFast = false` (the default), you typically see all validation errors in one go,
which can be especially useful for APIs and form validation.
