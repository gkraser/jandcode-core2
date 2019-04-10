package jandcode.commons;

import jandcode.commons.io.*;
import jandcode.commons.test.*;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

public class UtLoad_Test extends Utils_Test {

    @Test
    public void test1() throws Exception {
        StringLoader q = new StringLoader();
        UtLoad.fromString(q, "z-привет");
        assertEquals(q.getResult(), "z-привет");
    }


}
