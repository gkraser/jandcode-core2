package jandcode.commons.variant;

import jandcode.commons.test.*;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;


public class VariantMap_Test extends Utils_Test {

    @Test
    public void test1() throws Exception {
        VariantMap m = new VariantMap();
        m.setValue("k1", "123");
        assertEquals(m.getInt("k1"), 123);
        assertEquals(m.getLong("k1"), 123L);
        assertEquals(m.getDouble("k1"), 123.0, 0.01);
        assertEquals(m.getBoolean("k1", true), true);
        assertEquals(m.getBoolean("k1", false), false);
    }


}
