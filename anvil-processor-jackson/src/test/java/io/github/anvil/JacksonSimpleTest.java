package io.github.anvil;

import com.fasterxml.jackson.databind.node.ObjectNode;
import io.github.anvil.annotations.EnumValue;
import io.github.anvil.annotations.Validate;
import io.github.anvil.annotations.numeric.Equal;
import io.github.anvil.processor.JacksonProcessor;
import io.github.anvil.validation.validators.StringComparer.StringComparisonStrategy;
import org.junit.jupiter.api.Test;

import static io.github.anvil.TestUtils.getObjectNode;
import static org.assertj.core.api.Assertions.assertThat;

class JacksonSimpleTest {
    @Validate
    public static class A implements Schema {
        @Equal(10.2f)
        Float floatField;

        @Equal(10.2d)
        double doubleField;

        @Equal(10)
        int intField;

        @Equal(10)
        short shortField;

        @EnumValue(StringComparisonStrategy.class)
        StringComparisonStrategy stringComparisonStrategy;
    }

    @Test
    void process() {
        Anvil<ObjectNode> anvil = new Anvil<>(new JacksonProcessor());

        ObjectNode json = getObjectNode("""
                                            {
                                                "floatField": 10.2,
                                                "doubleField": 10.2,
                                                "intField": 10,
                                                "shortField": 10,
                                                "stringComparisonStrategy": "CASE_SENSITIVE"
                                            }
                                            """);

        A a = anvil.validate(json, A.class);
        assertThat(a.floatField).isEqualTo(10.2f);
        assertThat(a.doubleField).isEqualTo(10.2d);
        assertThat(a.intField).isEqualTo(10);
        assertThat(a.shortField).isEqualTo((short) 10);
        assertThat(a.stringComparisonStrategy).isEqualTo(StringComparisonStrategy.CASE_SENSITIVE);
    }
}
