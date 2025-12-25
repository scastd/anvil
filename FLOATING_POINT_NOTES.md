# Floating-Point Numbers in Numeric Annotations

## Overview of Floating-Point Types

Java provides two primitive floating-point types for representing decimal numbers:

### float (32-bit IEEE 754)

- **Size**: 4 bytes (32 bits).
- **Precision**: ~6-7 decimal digits.
- **Range**: ±3.40282347 × 10³⁸ (approximate).
- **Literal syntax**: Use `f` or `F` suffix (e.g., `3.14f`).

### double (64-bit IEEE 754)

- **Size**: 8 bytes (64 bits).
- **Precision**: ~15-16 decimal digits.
- **Range**: ±1.79769313486231570 × 10³⁰⁸ (approximate).
- **Literal syntax**: No suffix needed, or use `d`/`D` (e.g., `3.14` or `3.14d`).

## Important: Type Matching Rule

**When using numeric annotations with floating-point fields, the annotation value type MUST match the field type.**

### ✅ Correct Usage

```java

@Validate
class Example {
    // float field → use float annotation values
    @Greater(value = 0.0f)
    @LessOrEqual(value = 100.0f)
    private float temperature;

    // double field → use double annotation values
    @Equal(value = 3.14159265359)
    private double pi;

    @Between(min = -90.0f, max = 90.0f)
    private float latitude;
}
```

### ❌ Incorrect Usage

```java

@Validate
class Example {
    // WRONG: float field with double annotation value (no 'f' suffix)
    @Greater(value = 0.0)  // This is a double literal!
    private float temperature;

    // WRONG: double field with explicit float annotation value
    @Equal(value = 3.14f)  // This is a float literal!
    private double pi;
}
```

## Why Type Matching Matters

### 1. Precision Loss and Representation Differences

Floating-point numbers are stored using the IEEE 754 standard, which represents numbers in binary format. When
converting between `float` and `double`, precision differences can occur.

**Example:**

```java
float f = 0.1f;                // Stored with ~6-7 digits precision
double d = 0.1;                // Stored with ~15-16 digits precision
double fAsDouble = (double) f; // Converting f to double doesn't add precision
// fAsDouble may be 0.10000000149011612 due to float's limited precision
```

When a `float` field is compared against a `double` annotation value (and vice versa), the conversion can introduce
unexpected discrepancies in validation behavior.

### 2. Implicit Type Conversions

Java performs implicit widening conversions (e.g., `float` to `double`) but this doesn't preserve the exact
representation. The annotation framework must compare the field value with the annotation parameter, and type mismatches
force conversions that can alter the comparison semantics.

**Example:**

```java

@Equal(value = 1.1)  // This is a double: 1.1000000000000001 (approx in binary)
private float myValue = 1.1f;  // This is a float: 1.10000002384... (approx in binary)
// These two values are NOT equal due to different precision representations
```

### 3. Comparison Accuracy and Upcasting Behavior

Floating-point equality and comparison operations are extremely sensitive to the exact binary representation. Even tiny
differences introduced by type conversions can cause validation to fail or pass unexpectedly.

**Important Note on Float Comparisons:**
When comparing floating-point values in Java, both operands are upcasted to `double` before the comparison is performed.
This means:

- If both the field and annotation value are `float`, they are both converted to `double` during comparison.
- If one value is already `double` and the other is `float`, the `float` is converted to `double` during comparison.

**Example:**

```java
// Scenario 1: Matching types (both float)
@Equal(value = 1.1f)  // float literal
private float myValue = 1.1f;  // float field
// During comparison: both values are upcasted to double
// (double) 1.1f compared to (double) 1.1f → EQUAL (both have same precision loss)

// Scenario 2: Mismatched types (double annotation, float field)
@Equal(value = 1.1)  // double literal: 1.1000000000000001 (approx)
private float myValue = 1.1f;  // float literal: 1.10000002384... (approx)
// During comparison: myValue upcasted to double
// (double) 1.1f → 1.10000002384... compared to 1.1000000000000001 → NOT EQUAL
```

The validation logic compares values directly, and mixing types leads to different binary representations:

- When types match, both values undergo the same conversion path and maintain equality.
- When types mismatch, the values have different precision origins, leading to different binary representations after
  upcasting.

### 4. Predictable Behavior

Matching types ensures that the validation behaves exactly as the developer intends. When both the field and annotation
value are the same type, they undergo identical conversions during comparison, preserving the intended equality or
relationship. Type mismatches introduce asymmetric precision handling that can break expected validation logic.

## Example: Temperature Sensor

```java

@Validate
class TemperatureSensor {
    // High-precision temperature sensor using double
    @Between(min = -273.15, max = 5000.0)  // double values
    private double kelvinTemperature;

    // Low-precision sensor using float
    @Greater(value = 0.0f)  // float value with 'f' suffix
    @Less(value = 50.0f)    // float value with 'f' suffix
    private float celsiusTemperature;
}
```

## Summary

- **float fields** → annotation values must have `f` suffix (e.g., `1.5f`).
- **double fields** → annotation values should NOT have `f` suffix (e.g., `1.5`).
- Type matching prevents precision loss, implicit conversions, and unexpected validation behavior.
- The annotation value type must exactly match the field type for accurate and predictable validation.
