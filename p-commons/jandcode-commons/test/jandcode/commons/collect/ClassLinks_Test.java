package jandcode.commons.collect;

import jandcode.commons.test.*;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

public class ClassLinks_Test extends Utils_Test {

    class Data1 {
        String v;

        Data1(String v) {
            this.v = v;
        }

        public String toString() {
            return v;
        }
    }

    @Test
    public void testGet1() throws Exception {
        ClassLinks<Data1> z = new ClassLinks<Data1>();

        z.add(CharSequence.class, new Data1("CS"));
        //z.add(String.class, new Data1("ST"));
        z.add("null", new Data1("NULL"));
        z.add(Object.class, new Data1("OB"));

        assertEquals(z.get(String.class).toString(), "CS");
        assertEquals(z.get(StringBuilder.class).toString(), "CS");
        assertEquals(z.get(getClass()).toString(), "OB");

    }

    @Test
    public void testGet2() throws Exception {
        ClassLinks<Data1> z = new ClassLinks<Data1>();

        z.add(CharSequence.class, new Data1("CS"));
        z.add(String.class, new Data1("ST"));
        z.add("null", new Data1("NULL"));

        assertEquals(z.get(String.class).toString(), "ST");
        assertEquals(z.get(StringBuilder.class).toString(), "CS");

    }
}
