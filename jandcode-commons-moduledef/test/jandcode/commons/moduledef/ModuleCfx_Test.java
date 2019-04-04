package jandcode.commons.moduledef;

import jandcode.commons.conf.*;
import jandcode.commons.test.*;
import org.junit.jupiter.api.*;

import java.util.regex.*;

import static org.junit.jupiter.api.Assertions.*;

public class ModuleCfx_Test extends Utils_Test {

    @Test
    public void test_moduleVars() throws Exception {
        String modulePak = "jandcode.commons.moduledef.module1";
        String modulePath = modulePak.replace('.', '/');
        ModuleDef md = UtModuleDef.createModuleDef("jandcode-commons-moduledef-module1",
                utils.getTestFile("module1"),
                modulePak,
                utils.getTestFile("module1/module.cfx"));
        ModuleDefConfig mc = UtModuleDef.loadModuleDefConfig(md, null);
        Conf conf = mc.getConf();
//        String s = UtConf.save(conf).toString();
//        System.out.println(s);
        utils.outMap(conf);

        assertEquals(conf.getString("root/module.package"), modulePak);
        assertEquals(conf.getString("a/module.package"), modulePak);
        assertEquals(conf.getString("a.b/module.package"), modulePak);

        assertEquals(conf.getString("root/module.package.cur"), modulePak);
        assertEquals(conf.getString("a/module.package.cur"), modulePak + ".a1");
        assertEquals(conf.getString("a.b/module.package.cur"), modulePak + ".a1.b1");

        assertEquals(conf.getString("root/module.package.rel"), "");
        assertEquals(conf.getString("a/module.package.rel"), "a1");
        assertEquals(conf.getString("a.b/module.package.rel"), "a1.b1");

        assertEquals(conf.getString("root/module.packagepath"), modulePath);
        assertEquals(conf.getString("a/module.packagepath"), modulePath);
        assertEquals(conf.getString("a.b/module.packagepath"), modulePath);

        assertEquals(conf.getString("root/module.packagepath.cur"), modulePath);
        assertEquals(conf.getString("a/module.packagepath.cur"), modulePath + "/a1");
        assertEquals(conf.getString("a.b/module.packagepath.cur"), modulePath + "/a1/b1");

        assertEquals(conf.getString("root/module.packagepath.rel"), "");
        assertEquals(conf.getString("a/module.packagepath.rel"), "a1");
        assertEquals(conf.getString("a.b/module.packagepath.rel"), "a1/b1");

    }

    @Test
    public void test_regex() throws Exception {
        Pattern p = Pattern.compile("module\\[(.*)\\]\\.(.*)");

        String s = "module[jandcode.commons.moduledef.module1].path";
        Matcher m = p.matcher(s);
        if (m.find()) {
            System.out.println("module:" + m.group(1));
            System.out.println("var:" + m.group(2));
        }

    }

    @Test
    public void test_regex2() throws Exception {
        Pattern p = Pattern.compile("module\\.(.*)");

        String s = "module.path";
        Matcher m = p.matcher(s);
        if (m.find()) {
            System.out.println("var:" + m.group(1));
        }

    }


}
