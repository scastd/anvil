# Validators

This page explains how Anvil's validators work and how you can extend them with your own rules.

## What is a validator?

A validator is a small piece of logic that knows how to enforce a specific annotation contract.

- Each validator implements the `Validator` interface.
- Each validator is associated with a single annotation type.
- During validation, Anvil looks up the appropriate validator for every annotation on a field.

```java title="Validator.java"
public interface Validator {
    Object validate(Object value, String fieldName, Annotation annotation) throws ValidationError;

    Class<? extends Annotation> getSupportedAnnotation();
}
```

If a validator detects a problem, it throws `ValidationError`. If it returns a non-null value, that value becomes
the new value for the field (this allows for transformations such as trimming or normalization).

## Built-in validators

Anvil ships with validators for all built-in annotations:

- **Field-level**:
    - `OptionalValueValidator` - Supports the `@OptionalValue` marker annotation; optional behavior is handled by the
      processor.
- **String**:
    - `RegexValidator` - Implements `@Regex`.
    - `StrEqualValidator` - Implements `@StrEqual`.
    - `StrInValidator` - Implements `@StrIn`.
- **Enum**:
    - `EnumValueValidator` - Implements `@EnumValue`.
- **Numeric**:
    - `EqualValidator` - Implements `@Equal`.
    - `BetweenValidator` - Implements `@Between`.
    - `GreaterValidator` - Implements `@Greater`.
    - `GreaterOrEqualValidator` - Implements `@GreaterOrEqual`.
    - `LessValidator` - Implements `@Less`.
    - `LessOrEqualValidator` - Implements `@LessOrEqual`.
    - `InValidator` - Implements `@In`.
  - `InnerValidator` - Implements `@Inner`.

You typically do not use these classes directly. Instead, you apply the annotations on your schema and let Anvil
invoke the corresponding validators.

[//]: # (@formatter:off)

!!! info
    For the full list of annotations (with description and options), visit the [Annotations](annotations.md) guide.

[//]: # (@formatter:off)

## Execution order

For each field:

1. Anvil collects all annotations on the field.
2. Annotations are processed in a stable order.

If any validator throws `ValidationError`:

- In **fail-fast** mode, processing stops immediately and the error is propagated.
- Otherwise, the error is collected and validation continues with the remaining fields and annotations.

## Custom validators

In addition to the built-in validators, you can define your own annotations and validators that participate in
the same pipeline.

### 1. Create a custom annotation

```java title="StartsWith.java"
package com.example.validation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface StartsWith {
    String value();
}
```

### 2. Implement a `Validator`

Custom validators implement the `Validator` interface and declare which annotation they support.

```java title="StartsWithValidator.java"
package com.example.validation;

import io.github.anvil.validation.ValidationError;
import io.github.anvil.validation.Validator;

import java.lang.annotation.Annotation;

public class StartsWithValidator implements Validator {
    @Override
    public Object validate(Object value, String fieldName, Annotation annotation) throws ValidationError {
        StartsWith startsWith = (StartsWith) annotation;

        if (value == null) {
            return null;
        }

        String stringValue = value.toString();

        if (!stringValue.startsWith(startsWith.value())) {
            throw new ValidationError(
                "for field '%s': Must start with '%s'.".formatted(fieldName, startsWith.value())
            );
        }

        return value;
    }

    @Override
    public Class<? extends Annotation> getSupportedAnnotation() {
        return StartsWith.class;
    }
}
```

### 3. Register the validator

Custom validators are registered globally via the singleton `ValidatorRegistry`:

[//]: # (@formatter:off)
```java
import io.github.anvil.validation.ValidatorRegistry;

ValidatorRegistry.getInstance().addValidator(new StartsWithValidator());
```
[//]: # (@formatter:on)

Once registered, any field annotated with `@StartsWith` will be validated by your `StartsWithValidator` as part of
the normal annotation processing flow in all processors.
