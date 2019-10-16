package jandcode.jsa.jsmodule;

import jandcode.core.web.test.*;
import jandcode.jsa.jsmodule.impl.*;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

public class ModuleNameResolver_Test extends Web_Test {

    JsModuleService svc;
    ModuleNameResolver r;

    public void setUp() throws Exception {
        super.setUp();
        //
        svc = app.bean(JsModuleService.class);
        r = new ModuleNameResolver(svc);
    }

    @Test
    public void test1() throws Exception {
        String s;

        s = r.resolveModuleName("jandcode.jsa");
        assertEquals(s, "jandcode/jsa");

        s = r.resolveModuleName("jandcode.jsa/path1/path2");
        assertEquals(s, "jandcode/jsa/path1/path2");
    }

    @Test
    public void test2_0() throws Exception {
        String s;

        s = r.resolveModuleName("jandcode.jsa/data/mods/m1.js");
        assertEquals(s, "jandcode/jsa/data/mods/m1.js");
    }

    @Test
    public void test2_1() throws Exception {
        String s;

        s = r.resolveModuleName("jandcode.jsa/data/mods/m1");
        assertEquals(s, "jandcode/jsa/data/mods/m1.js");
    }

    @Test
    public void test2_2() throws Exception {
        String s;

        s = r.resolveModuleName("/jandcode.jsa/data/mods/m1");
        assertEquals(s, "jandcode/jsa/data/mods/m1.js");
    }

    @Test
    public void test3() throws Exception {
        String s;

        s = r.resolveModuleName("jandcode.jsa/data/mods/m4");
        assertEquals(s, "jandcode/jsa/data/mods/m4/index.js");
    }

    @Test
    public void test4() throws Exception {
        String s;

        s = r.resolveModuleName("_jsa/_node_modules/jquery");
        assertEquals(s, "_jsa/_node_modules/jquery/dist/jquery.js");
    }

    @Test
    public void test5() throws Exception {
        String s;

        s = r.resolveModuleName("jquery");
        assertEquals(s, "_jsa/_node_modules/jquery/dist/jquery.js");
    }


    @Test
    public void test6() throws Exception {
        String s;

        s = r.resolveModuleName("mocha/mocha");
        assertEquals(s, "_jsa/_node_modules/mocha/mocha.js");
    }

    @Test
    public void test7() throws Exception {
        String s;

        s = r.resolveModuleName("chai-nm");
        assertEquals(s, "_jsa/_node_modules/chai-nm/index.js");
    }

    @Test
    public void test8() throws Exception {
        String s;

        s = r.resolveModuleName("chai/chai");
        assertEquals(s, "_jsa/_node_modules/chai/chai.js");
    }


}
