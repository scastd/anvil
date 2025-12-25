package io.github.anvil;

import com.google.gson.JsonObject;
import io.github.anvil.annotations.OptionalValue;
import io.github.anvil.annotations.StrEqual;
import io.github.anvil.annotations.Validate;
import io.github.anvil.annotations.numeric.Equal;
import io.github.anvil.processor.GsonProcessor;
import org.junit.jupiter.api.Test;

import static io.github.anvil.TestUtils.getJsonObject;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class GsonSimpleTest {
    @Validate
    public static class NoArgsConstructorClass implements Schema {
        @Equal(10.2f)
        Float floatField;

        @Equal(10.2d)
        double doubleField;

        @Equal(10)
        int intField;

        @Equal(10)
        short shortField;

        @OptionalValue
        @StrEqual("test")
        String stringField;
    }

    @Test
    void processNoArgsConstructor() {
        Anvil<JsonObject> anvil = new Anvil<>(new GsonProcessor());

        JsonObject json = getJsonObject("""
                                            {
                                                "floatField": 10.2,
                                                "doubleField": 10.2,
                                                "intField": 10,
                                                "shortField": 10
                                            }
                                            """);

        NoArgsConstructorClass validated = anvil.validate(json, NoArgsConstructorClass.class);
        assertEquals(10.2f, validated.floatField);
        assertEquals(10.2d, validated.doubleField);
        assertEquals(10, validated.intField);
        assertEquals(10, validated.shortField);
        assertNull(validated.stringField);
    }

    @Validate
    public static class AllArgsConstructorClass implements Schema {
        @Equal(10.2f)
        Float floatField;

        @Equal(10.2d)
        double doubleField;

        @Equal(10)
        int intField;

        @Equal(10)
        short shortField;

        @OptionalValue
        @StrEqual("test")
        String stringField;

        public AllArgsConstructorClass(
            Float floatField,
            double doubleField,
            int intField,
            short shortField,
            String stringField
        ) {
            this.floatField = floatField;
            this.doubleField = doubleField;
            this.intField = intField;
            this.shortField = shortField;
            this.stringField = stringField;
        }
    }

    @Test
    void processAllArgsConstructor() {
        Anvil<JsonObject> anvil = new Anvil<>(new GsonProcessor());

        JsonObject json = getJsonObject("""
                                            {
                                                "floatField": 10.2,
                                                "doubleField": 10.2,
                                                "intField": 10,
                                                "shortField": 10
                                            }
                                            """);

        AllArgsConstructorClass validated = anvil.validate(json, AllArgsConstructorClass.class);
        assertEquals(10.2f, validated.floatField);
        assertEquals(10.2d, validated.doubleField);
        assertEquals(10, validated.intField);
        assertEquals(10, validated.shortField);
        assertNull(validated.stringField);
    }

    @Validate
    public record TestRecord(
        @Equal(10.2f)
        Float floatField,

        @Equal(10.2d)
        double doubleField,

        @Equal(10)
        int intField,

        @Equal(10)
        short shortField,

        @OptionalValue
        @StrEqual("test")
        String stringField
    ) implements Schema {
    }

    @Test
    void processRecord() {
        Anvil<JsonObject> anvil = new Anvil<>(new GsonProcessor());

        JsonObject json = getJsonObject("""
                                            {
                                                "floatField": 10.2,
                                                "doubleField": 10.2,
                                                "intField": 10,
                                                "shortField": 10
                                            }
                                            """);

        TestRecord validated = anvil.validate(json, TestRecord.class);
        assertEquals(10.2f, validated.floatField);
        assertEquals(10.2d, validated.doubleField);
        assertEquals(10, validated.intField);
        assertEquals(10, validated.shortField);
        assertNull(validated.stringField);
    }
}
