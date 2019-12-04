package jandcode.core.web.action;

import jandcode.core.web.test.*;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

public class ActionMethod_Test extends Web_Test {

    @Test
    public void test1() throws Exception {
        String s;

        s = web.execRequest("actionMethod/m1");
        assertEquals(s, "m1-render");

        s = web.execRequest("actionMethod/m2");
        assertEquals(s, "m2-render");

        s = web.execRequest("actionMethod/m3");
        assertEquals(s, "**m3-render}}");
    }


}
