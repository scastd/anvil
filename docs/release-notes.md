# Release notes

## 0.0.1

_Date: 2025-11-29_

### ✨ Features

- Initial public release of **Anvil**.
- **Core validation library** (`anvil-core`) with:
    - `Schema` base class and `Anvil` orchestrator.
    - Rich set of validation annotations for strings, numbers, and schema-level rules.
        - Schema annotation: `@Validate`.
        - Field annotation: `@ValidateField` with `required` flag for optional fields.
        - String annotations: `@Regex`, `@StrEqual`, `@StrIn`.
        - Enum annotation: `@EnumValue`.
        - Numeric annotations: `@Equal`, `@Between`, `@Greater`, `@GreaterOrEqual`, `@Less`, `@LessOrEqual`, `@In`.
    - Support for pre- and post-build hooks on schemas.
- **Gson processor** (`anvil-processor-gson`) with `GsonProcessor` integration.
- **Jackson processor** (`anvil-processor-jackson`) with `JacksonProcessor` integration.
- **Advanced validation features**:
    - Fail-fast mode via `@Validate(failFast = true)`.
    - Debug/print mode via `@Validate(printInfo = true)`.
    - Detailed, field-level validation errors.
    - Aggregated error reporting via `ValidationException` and `ValidationError`.
- **Extensibility**:
    - Custom processors by extending `Processor<IN>` for alternative JSON or input types.
    - Pluggable validators via the `Validator` API and registry.
