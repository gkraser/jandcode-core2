package jandcode.core.jsa.jsmodule;

import jandcode.core.web.test.*;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

public class JsModule_Test extends Web_Test {

    JsModuleService svc;

    public void setUp() throws Exception {
        super.setUp();
        svc = app.bean(JsModuleService.class);
    }

    @Test
    public void req1_jquery() throws Exception {
        JsModule m = svc.findModule(svc.resolveModuleName("jquery"));

        System.out.println(m);
        System.out.println("name=" + m.getName());
        System.out.println("reqs=");
        for (RequireItem m1 : m.getRequires()) {
            System.out.println("  " + m1);
        }
        for (JsModule m1 : m.getRequiresExpanded()) {
            System.out.println("  " + m1);
        }
    }

    @Test
    public void req1() throws Exception {
        JsModule m = svc.findModule("jandcode/core/jsa/data/mods/m3.js");

        System.out.println(m);
        System.out.println("name=" + m.getName());
        System.out.println("reqs=");
        for (RequireItem m1 : m.getRequires()) {
            System.out.println("  " + m1);
        }
        for (JsModule m1 : m.getRequiresExpanded()) {
            System.out.println("  " + m1);
        }
    }


    @Test
    public void gsp1() throws Exception {
        JsModule m = svc.findModule("jandcode/core/jsa/data/mods/m5.js");

        System.out.println(m);
        System.out.println("name=" + m.getName());
        System.out.println("reqs=");
        for (RequireItem m1 : m.getRequires()) {
            System.out.println("  " + m1);
        }
        for (JsModule m1 : m.getRequiresExpanded()) {
            System.out.println("  " + m1);
        }
        System.out.println("text=" + m.getText());
    }

    @Test
    public void dynamic1() throws Exception {
        JsModule m = svc.findModule("jandcode/core/jsa/data/mods/m6-dynamic.js");

        System.out.println(m);
        System.out.println("name=" + m.getName());
        System.out.println("hash=" + m.getHash());
        System.out.println("reqs=");
        for (RequireItem m1 : m.getRequires()) {
            System.out.println("  " + m1);
        }
        for (JsModule m1 : m.getRequiresExpanded()) {
            System.out.println("  " + m1);
        }
        System.out.println("text=" + m.getText());
        Thread.sleep(50);
        System.out.println("text=" + m.getText());
    }

    @Test
    public void importModule1() throws Exception {
        JsModule m = svc.findModule("jandcode/core/jsa/data/mods/m7-import-module.js");

        System.out.println(m);
        System.out.println("name=" + m.getName());
        System.out.println("hash=" + m.getHash());
        System.out.println("reqs=");
        for (RequireItem m1 : m.getRequires()) {
            System.out.println("  " + m1);
        }
        for (JsModule m1 : m.getRequiresExpanded()) {
            System.out.println("  " + m1);
        }
        System.out.println("text=" + m.getText());
    }

    @Test
    public void css1() throws Exception {
        JsModule m = svc.findModule("jandcode/core/jsa/data/css/q1.css");

        System.out.println(m);
        System.out.println("name=" + m.getName());
        System.out.println("hash=" + m.getHash());
        System.out.println("reqs=");
        for (RequireItem m1 : m.getRequires()) {
            System.out.println("  " + m1);
        }
        for (JsModule m1 : m.getRequiresExpanded()) {
            System.out.println("  " + m1);
        }
        System.out.println("text=" + m.getText());
    }

    @Test
    public void less1() throws Exception {
        JsModule m = svc.findModule("jandcode/core/jsa/data/css/q3.less");

        System.out.println(m);
        System.out.println("name=" + m.getName());
        System.out.println("hash=" + m.getHash());
        System.out.println("reqs=");
        for (RequireItem m1 : m.getRequires()) {
            System.out.println("  " + m1);
        }
        for (JsModule m1 : m.getRequiresExpanded()) {
            System.out.println("  " + m1);
        }
        System.out.println("text=" + m.getText());
    }

    @Test
    public void reqExternal1() throws Exception {
        JsModule m = svc.findModule("jandcode/core/jsa/data/mods/m8-req.js");

        System.out.println(m);
        System.out.println("name=" + m.getName());
        System.out.println("hash=" + m.getHash());
        System.out.println("reqs=");
        for (RequireItem m1 : m.getRequires()) {
            System.out.println("  " + m1);
        }
        //
        assertEquals(m.getRequires().toString(), "[./m1=>[jandcode/core/jsa/data/mods/m1.js], ./m2=>[jandcode/core/jsa/data/mods/m2.js]]");
    }

}
