package jandcode.core.web.action;

import jandcode.core.web.test.*;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

public class ActionRender_Test extends Web_Test {

    @Test
    public void map1() throws Exception {
        String s = web.execRequest("actionRender/map1");
        assertEquals(s, "{\"a\":\"b\"}");
    }

    @Test
    public void list1() throws Exception {
        String s = web.execRequest("actionRender/list1");
        assertEquals(s, "[\"a\",\"b\"]");
    }


}
