package io.github.anvil;

import com.google.gson.JsonObject;
import io.github.anvil.annotations.StrEqual;
import io.github.anvil.annotations.Validate;
import io.github.anvil.annotations.ValidateField;
import io.github.anvil.annotations.numeric.Equal;
import io.github.anvil.processor.GsonProcessor;
import org.junit.jupiter.api.Test;

import static io.github.anvil.TestUtils.getJsonObject;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class GsonSimpleTest {
    @Validate
    public static class A implements Schema {
        @Equal(10.2f)
        @ValidateField
        Float floatField;

        @Equal(10.2d)
        @ValidateField
        double doubleField;

        @Equal(10)
        @ValidateField
        int intField;

        @Equal(10)
        @ValidateField
        short shortField;

        // @ValidateField
        @ValidateField(required = false)
        @StrEqual("test")
        String stringField;
    }

    @Test
    void process() {
        Anvil<JsonObject> anvil = new Anvil<>(new GsonProcessor());

        JsonObject json = getJsonObject("""
                                            {
                                                "floatField": 10.2,
                                                "doubleField": 10.2,
                                                "intField": 10,
                                                "shortField": 10
                                            }
                                            """);

        A a = anvil.validate(json, A.class);
        assertEquals(10.2f, a.floatField);
        assertEquals(10.2d, a.doubleField);
        assertEquals(10, a.intField);
        assertEquals(10, a.shortField);
        assertNull(a.stringField);
    }
}
