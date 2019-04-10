package jandcode.commons.io;

import jandcode.commons.*;
import jandcode.commons.test.*;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

public class StringLoader_Test extends Utils_Test {

    @Test
    public void test1() throws Exception {
        StringLoader h = new StringLoader();
        UtLoad.fromString(h, "hello");
        assertEquals(h.getResult(), "hello");
    }


}
