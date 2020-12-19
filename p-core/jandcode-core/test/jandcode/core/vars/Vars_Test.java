package jandcode.core.vars;

import jandcode.commons.*;
import jandcode.core.test.*;
import org.junit.jupiter.api.*;

public class Vars_Test extends App_Test {

    @Test
    public void test1() throws Exception {
        String appDir = app.getAppdir();
        //
        String s = UtConf.save(app.getConf()).toString();
        System.out.println(s);
    }


}
