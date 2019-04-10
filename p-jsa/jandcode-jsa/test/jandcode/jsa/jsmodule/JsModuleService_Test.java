package jandcode.jsa.jsmodule;

import jandcode.web.test.*;
import org.junit.jupiter.api.*;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class JsModuleService_Test extends Web_Test {

    @Test
    public void resolveRequire1() throws Exception {
        JsModuleService svc = app.bean(JsModuleService.class);

        List<String> a;

        a = svc.resolveRequire("jandcode/jsa/data/mods", "./m1.js");
        assertEquals(a.toString(), "[jandcode/jsa/data/mods/m1.js]");

        a = svc.resolveRequire("jandcode/jsa/data/mods", "jandcode/jsa/data/mods/m1.js");
        assertEquals(a.toString(), "[jandcode/jsa/data/mods/m1.js]");

        a = svc.resolveRequire("jandcode/jsa/data/mods", "./m1*.js");
        assertEquals(a.toString(), "[jandcode/jsa/data/mods/m1.js]");

    }


}
