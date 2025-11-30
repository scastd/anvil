package io.github.anvil.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Validates that a field value is a valid constant of the specified {@link Enum} type.
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface EnumValue {

    /**
     * The enum type that defines the allowed values for the annotated field.
     *
     * @return the enum class to validate against.
     */
    Class<? extends Enum<?>> value();
}
