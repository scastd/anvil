package io.github.anvil.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marks a field as part of a {@link Validate}d object and controls whether
 * it is required during validation.
 *
 * <p>If {@link #required()} is {@code true} (default), the field must be present
 * and non-null for validation to succeed. If set to {@code false}, the field
 * is considered optional and will be validated only when present.</p>
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidateField {

    /**
     * Indicates whether the annotated field is required to be present.
     *
     * @return {@code true} if the field is required, {@code false} if it is optional.
     */
    boolean required() default true;
}
