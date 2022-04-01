package jandcode.core.dbm.verdb;

import jandcode.commons.test.*;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

public class VerdbVersion_Test extends Utils_Test {

    @Test
    public void create_num1() throws Exception {
        VerdbVersion v;

        v = VerdbVersion.create(4, 6, 7);
        assertEquals(v.toString(), "4.6.7");
    }

    @Test
    public void create_string1() throws Exception {
        VerdbVersion v;

        v = VerdbVersion.create(null);
        assertEquals(v.toString(), "0.0.0");

        v = VerdbVersion.create("");
        assertEquals(v.toString(), "0.0.0");

        v = VerdbVersion.create("1");
        assertEquals(v.toString(), "1.0.0");

        v = VerdbVersion.create("1.2");
        assertEquals(v.toString(), "1.2.0");

        v = VerdbVersion.create("1.2.3");
        assertEquals(v.toString(), "1.2.3");

        v = VerdbVersion.create("1.2.3.4");
        assertEquals(v.toString(), "1.2.3");
    }

    @Test
    public void compare1() throws Exception {
        var v1 = VerdbVersion.create("1.2.3");
        var v2 = VerdbVersion.create("1.2.3");

        assertEquals(v1.compareTo(v2), 0);
        assertTrue(v1.equals(v2));

        v2 = VerdbVersion.create("1.1");
        assertEquals(v1.compareTo(v2), 1);

        v2 = VerdbVersion.create("1.3");
        assertEquals(v1.compareTo(v2), -1);

        assertFalse(v1.equals(v2));
    }

    @Test
    public void v1v2v3() throws Exception {
        var v1 = VerdbVersion.create("1.2.3");
        assertEquals(v1.getV1(), 1);
        assertEquals(v1.getV2(), 2);
        assertEquals(v1.getV3(), 3);
    }

    @Test
    public void with1() throws Exception {
        VerdbVersion v;

        v = VerdbVersion.create(4, 6, 7);
        assertEquals(v.with(-1, 8, -1).toString(), "4.8.7");
    }

}
