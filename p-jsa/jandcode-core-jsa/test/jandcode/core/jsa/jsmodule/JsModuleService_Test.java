package jandcode.core.jsa.jsmodule;

import jandcode.core.web.test.*;
import org.junit.jupiter.api.*;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class JsModuleService_Test extends Web_Test {

    @Test
    public void resolveRequire1() throws Exception {
        JsModuleService svc = app.bean(JsModuleService.class);

        List<String> a;

        a = svc.resolveRequire("jandcode/core/jsa/data/mods", "./m1.js");
        assertEquals(a.toString(), "[jandcode/core/jsa/data/mods/m1.js]");

        a = svc.resolveRequire("jandcode/core/jsa/data/mods", "jandcode/core/jsa/data/mods/m1.js");
        assertEquals(a.toString(), "[jandcode/core/jsa/data/mods/m1.js]");

        a = svc.resolveRequire("jandcode/core/jsa/data/mods", "./m1*.js");
        assertEquals(a.toString(), "[jandcode/core/jsa/data/mods/m1.js]");

    }


}
