/*
 * Copyright 2025-present Samuel Castrillo
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.github.anvil.annotations;

import io.github.anvil.Schema;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marks a field as containing a list of elements of a specified schema type that should be validated independently.
 *
 * <p>When a field is annotated with {@code @List}, each element in the list is validated using the schema class
 * specified by the annotation's {@code value}. The validation follows the same rules as top-level schema validation,
 * including all field annotations and validation rules defined in the specified schema class.</p>
 *
 * <p>This annotation enables validation of lists of nested structures. Each element in the list is validated
 * before being assigned to the field, ensuring that all validation rules are applied recursively.</p>
 *
 * <p>Example usage:</p>
 * <pre>{@code
 * @Validate
 * public class Item implements Schema {
 *     private String name;
 *     private int quantity;
 * }
 *
 * @Validate
 * public class Order implements Schema {
 *     private String orderId;
 *
 *     @List(Item.class)
 *     private List<Item> items;
 * }
 * }</pre>
 *
 * <p>In the above example, when validating an {@code Order}, each element in the {@code items} list will be
 * validated as an {@code Item} schema, ensuring that both the order and its items are valid.</p>
 *
 * @see Schema
 * @see Validate
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface List {
    /**
     * Specifies the class type of the elements contained within the list.
     *
     * @return the class type of the list elements.
     */
    Class<? extends Schema> value();
}
