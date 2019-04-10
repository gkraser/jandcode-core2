package jandcode.web;

import jandcode.core.test.*;
import jandcode.web.action.*;
import org.junit.jupiter.api.*;

public class Actions_Test extends App_Test {

    @Test
    public void test1() throws Exception {
        WebService svc = app.bean(WebService.class);
        for (ActionDef d : svc.getActions()) {
            IAction a = d.createInst();
            System.out.println(d.getName() + "=" + a.getClass());
        }
    }

}
