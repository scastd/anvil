# Release notes

## 0.1.0

_Date: 2026-01-17_

### ⚠️ BREAKING CHANGES

* **Annotations:** Replaced `ValidateField` annotations to use `OptionalValue` when
  needed ([#1](https://github.com/scastd/anvil/pull/1)).
* **Architecture:** Changed `Schema` to an **interface** instead of a class. Existing implementations extending `Schema`
  must be updated to implement it.

### ✨ Features

* **Nested Validation:** Added `@Inner` annotation for nested schema
  validation ([#3](https://github.com/scastd/anvil/pull/3)).
* **List Validation:** Added `@List` annotation for validating list/collection
  fields ([#6](https://github.com/scastd/anvil/pull/6)).
* **UUID String Validation:** Added `@UUID` annotation for validating UUID format
  strings ([#5](https://github.com/scastd/anvil/pull/5)).
* **Error Handling:** Enhanced validation error handling with the new `ValidationErrors`
  class ([#2](https://github.com/scastd/anvil/pull/2)).
* **Validator Registry:**
    * Implemented `addValidator` in the registry to allow custom validator additions.
    * Refactored validators to be non-static and registered via the constructor.
* **General:** Added additional restriction logic.

### 🐛 Bug Fixes

* **Legal:** Added missing copyright notices to every project file.

### ♻️ Code Refactoring

* **Tests:** Updated assertions in validator tests to use **AssertJ**.
* **Logic:**
    * Updated `processUnchecked` method to use a more specific class type for `outClass`.
    * Moved inner validator registration to the constructor and streamlined field checks.
    * Simplified various conditionals and replaced lambdas with method references.
* **Error Messages:** Standardized all error message formats across the entire library to use a consistent
  `"for field '%s': <error message>."` format ([#4](https://github.com/scastd/anvil/pull/4)). All error messages now:
    * Use the standardized `"for field '%s':"` prefix for field-specific errors.
    * End with a period (`.`) for consistency.
    * Follow a uniform structure for easier parsing and processing.

### 📚 Documentation

* **Error Messages:** Updated all documentation examples to reflect the standardized error message
  format ([#4](https://github.com/scastd/anvil/pull/4)).

### 👷 Build & CI

* Configured auto-publishing to Maven Central on release.
* Updated CI conditions to trigger documentation publishing on tag events.

## 0.0.6

_Date: 2025-12-01_

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
