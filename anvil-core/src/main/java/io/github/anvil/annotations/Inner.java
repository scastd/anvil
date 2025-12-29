package io.github.anvil.annotations;

import io.github.anvil.Schema;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marks a field as containing a nested schema that should be validated independently.
 *
 * <p>When a field is annotated with {@code @Inner}, the field's value is extracted from the input
 * as a nested object and validated using the schema class specified by the annotation's {@code value}.
 * The nested validation follows the same rules as top-level schema validation, including all field
 * annotations and validation rules defined in the nested schema class.</p>
 *
 * <p>This annotation enables validation of nested structures. The nested object is validated
 * before being assigned to the field, ensuring that all validation rules are applied recursively.</p>
 *
 * <p>Example usage:</p>
 * <pre>{@code
 * @Validate
 * public class Address implements Schema {
 *     private String street;
 *     private String city;
 * }
 *
 * @Validate
 * public class User implements Schema {
 *     private String name;
 *
 *     @Inner(Address.class)
 *     private Address address;
 * }
 * }</pre>
 *
 * <p>In the above example, when validating a {@code User}, the {@code address} field will be
 * validated as an {@code Address} schema, ensuring that both the user and address data are valid.</p>
 *
 * @see Schema
 * @see Validate
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Inner {
    /**
     * The schema class to use for validating the nested object.
     *
     * @return the schema class that defines the validation rules for the nested field.
     */
    Class<? extends Schema> value();
}
