package jandcode.core.jsa;

import jandcode.core.jsa.cfg.*;
import jandcode.core.test.*;
import org.junit.jupiter.api.*;

public class JsaCfg_Test extends App_Test {

    @Test
    public void test1() throws Exception {
        String s = app.bean(JsaCfg.class).getBootModule();
        System.out.println(s);
    }


}
