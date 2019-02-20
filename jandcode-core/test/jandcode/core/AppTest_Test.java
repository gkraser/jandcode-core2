package jandcode.core;

import jandcode.commons.*;
import jandcode.core.test.*;
import org.junit.jupiter.api.*;

public class AppTest_Test extends App_Test {

    @Test
    public void test1() throws Exception {
        for (Module m : app.getModules()) {
            System.out.println(m.getName() + " => " + m.getPath());
        }
        System.out.println(UtConf.save(app.getConf()).toString());
    }

    @Test
    public void test2() throws Exception {
        for (BeanDef m : app.getBeanFactory().getBeans()) {
            System.out.println(m.getName() + " => " + m.getCls());
        }
        System.out.println(UtConf.save(app.getConf()).toString());
    }


}
