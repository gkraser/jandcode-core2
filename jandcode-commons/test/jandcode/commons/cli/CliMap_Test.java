package jandcode.commons.cli;

import jandcode.commons.test.*;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

public class CliMap_Test extends Utils_Test {

    @Test
    public void test1() throws Exception {
        CliMap d = new CliMap();
        d.parse(new String[]{
                "-a", "-b:2", "-b:3", "p1", "p2", "-c", "-d:"
        });
        assertEquals(d.getParams().size(), 2);
        assertEquals(d.get("a"), "");
        assertEquals(d.get("b"), "3");
        assertEquals(d.get("c"), "");
        assertEquals(d.get("d"), "");
    }

}
