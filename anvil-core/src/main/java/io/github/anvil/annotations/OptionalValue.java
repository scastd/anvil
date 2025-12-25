package io.github.anvil.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marks a field as optional during validation.
 *
 * <p>When a field is annotated with {@code @OptionalValue} and the corresponding input
 * value is not provided (i.e. {@code null}), Anvil will not report a validation error
 * for that field. If a value is present, all other validation annotations are applied
 * as usual.</p>
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface OptionalValue {
}
