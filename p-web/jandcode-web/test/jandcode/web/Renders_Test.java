package jandcode.web;

import jandcode.core.test.*;
import jandcode.web.render.*;
import org.junit.jupiter.api.*;

public class Renders_Test extends App_Test {

    @Test
    public void test1() throws Exception {
        WebService svc = app.bean(WebService.class);
        for (RenderDef d : svc.getRenders()) {
            IRender a = d.createInst();
            System.out.println(d.getName() + "=" + a.getClass());
        }
    }

}
