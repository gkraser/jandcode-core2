package jandcode.jsa.jsmodule;

import jandcode.commons.test.*;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

public class JsModuleUtils_Test extends Utils_Test {

    @Test
    public void test_strToIdList() throws Exception {
        String s = "12345678";
        assertEquals(JsModuleUtils.strToIdList(s).toString(), "[12345678]");
        s = "12345678qwertyui";
        assertEquals(JsModuleUtils.strToIdList(s).toString(), "[12345678, qwertyui]");
    }

    @Test
    public void test_id() throws Exception {
        String id = JsModuleUtils.pathToId("a/b/c.js");
        assertEquals(id, "qmYrkVJn");
    }

}
