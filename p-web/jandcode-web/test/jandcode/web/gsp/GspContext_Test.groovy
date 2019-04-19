package jandcode.web.gsp

import jandcode.web.*
import jandcode.web.std.gsp.*
import jandcode.web.test.*
import org.junit.jupiter.api.*

import static org.junit.jupiter.api.Assertions.*

class GspContext_Test extends Web_Test {

    WebService svc

    void setUp() throws Exception {
        super.setUp()

        svc = app.bean(WebService)
    }

    @Test
    public void inst1() throws Exception {
        GspContext ctx1 = svc.createGspContext()
        GspPageManager p1 = ctx1.inst(GspPageManager)
        GspPageManager p1_1 = ctx1.inst(GspPageManager)
        assertSame(p1, p1_1)

        GspContext ctx2 = svc.createGspContext()
        GspPageManager p2 = ctx2.inst(GspPageManager)
        assertNotSame(p1, p2)
    }

    @Test
    public void bean1() throws Exception {
        GspContext ctx1 = svc.createGspContext()
        try {
            // bean в контексте недоступен, только inst!
            GspPageManager p1 = ctx1.bean(GspPageManager)
            fail()
        } catch (e) {
            // ignore
        }
    }

}
