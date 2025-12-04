# Error Handling

Anvil separates **validation errors** from other exceptions so you can reliably report problems back to callers.

## Error types

Anvil uses two main error types during validation:

- `ValidationError` - A single validation problem, thrown by validators or schema lifecycle hooks.
- `ValidationException` - A runtime exception thrown by `Anvil.validate(...)` that aggregates one or more
  `ValidationError` instances into a human-readable message.

`ValidationError` is never thrown directly from your application code in normal usage. Instead, the `Processor` catches
these errors and either:

- Re-throws them immediately when `failFast = true`.
- Collects them into a list and eventually wraps them in a `ValidationException` when validation completes.

## Fail-fast vs aggregated errors

Whether validation stops on the first error or accumulates all errors is controlled by the `failFast` attribute
on `@Validate`:

```java

@Validate(failFast = true)
public class User implements Schema {
    @ValidateField
    private String username;

    @ValidateField
    @GreaterOrEqual(18)
    private int age;
}
```

- When `failFast = true`, the first `ValidationError` thrown by a validator or lifecycle hook is immediately propagated
  and wrapped into a `ValidationException`. You will typically see a single error in the exception message.
- When `failFast = false` (the default), all validators run, all `ValidationError` instances are collected, and
  `Anvil.validate(...)` throws a `ValidationException` containing the complete list.

Use **fail-fast** mode when:

- Only the first error is interesting to the caller.
- You want to short-circuit expensive validations after a failure.

Use **aggregated** mode when:

- You want to show users all validation issues in one response (e.g. form or API payload validation).

## Handling `ValidationException`

Most applications interact with Anvil through `Anvil.validate(...)` and catch `ValidationException` at the boundary
layer (e.g. HTTP controller, message consumer, CLI entrypoint).

### Basic try/catch

[//]: # (@formatter:off)
```java
try {
    User user = anvil.validate(input, User.class);
    // Use the validated user
} catch (ValidationException e) {
    // Log and return an appropriate response
    logger.warn("Validation failed: {}", e.getMessage());
}
```
[//]: # (@formatter:on)

`ValidationException#getMessage()` already contains a formatted summary of all `ValidationError` messages. If you need
structured error data (e.g. field names and messages), wrap the call to `validate` and parse or extend the exception
type to suit your needs.

### Mapping to HTTP responses

When using Anvil in a web API, a common pattern is to convert `ValidationException` into a `400 Bad Request` with
an error payload:

[//]: # (@formatter:off)
```java
try {
    User user = anvil.validate(jsonBody, User.class);
    // continue with business logic
} catch (ValidationException e) {
    return ResponseEntity
        .badRequest()
        .body(Map.of("error", "validation_failed", "details", e.getMessage()));
}
```
[//]: # (@formatter:on)

You can adapt this pattern for your framework (Spring, Micronaut, Quarkus, etc.) using global exception handlers or
filters so you do not repeat the mapping logic in every controller.

## Non-validation exceptions

Anvil may also throw other unchecked exceptions in misconfiguration scenarios, for example:

- A schema class is not annotated with `@Validate` but is passed to `validate(...)`.
- Validation is explicitly disabled via `@Validate(value = false)`.
- A schema field is missing `@ValidateField`.
- A schema has an invalid combination of annotations that violates restrictions.
- A schema cannot be instantiated because it has no accessible no-args constructor.
- A processor encounters an unsupported numeric type and throws an `IllegalArgumentException`.
- There is no registered validator for a given annotation type.

These indicate **programmer errors** rather than user input problems. They should generally be treated as bugs and
fixed in code (for example by adding `@ValidateField` or adjusting the schema).

In production systems you might still want to log these exceptions and return a generic `500 Internal Server Error`
to callers, while using monitoring/alerts to detect and fix the underlying configuration issues.


