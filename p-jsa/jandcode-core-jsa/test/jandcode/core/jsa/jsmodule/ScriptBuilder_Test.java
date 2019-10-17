package jandcode.core.jsa.jsmodule;

import jandcode.core.jsa.jsmodule.impl.*;
import jandcode.core.web.test.*;
import org.junit.jupiter.api.*;

public class ScriptBuilder_Test extends Web_Test {

    JsModuleService svc;

    public void setUp() throws Exception {
        super.setUp();
        svc = app.bean(JsModuleService.class);
    }

    @Test
    public void moduleText1() throws Exception {
        ModuleScriptBuilder b = new ModuleScriptBuilder();

        JsModule m = svc.getModule("jandcode/core/jsa/data/mods/m1.js");

        String s = b.build(m, false);
        System.out.println(s);

        s = b.build(m, true);
        System.out.println(s);
    }


}
