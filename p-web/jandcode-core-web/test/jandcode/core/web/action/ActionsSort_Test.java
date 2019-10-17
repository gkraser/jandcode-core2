package jandcode.core.web.action;

import jandcode.core.test.*;
import jandcode.core.web.*;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

public class ActionsSort_Test extends App_Test {

    @Test
    public void test1() throws Exception {
        WebService svc = app.bean(WebService.class);
        for (ActionDef d : svc.getActions()) {
            IAction a = d.createInst();
            System.out.println(d.getName() + "=" + a.getClass());
        }
        assertEquals(svc.getActions().get(0).getName(), "z1/a/b");
        assertEquals(svc.getActions().get(svc.getActions().size() - 1).getName(), "z1");
    }

}
