package jandcode.core.web.gsp

import jandcode.core.web.*
import jandcode.core.web.test.*
import org.junit.jupiter.api.*

import static org.junit.jupiter.api.Assertions.*

class GspLaterOut_Test extends Web_Test {

    WebService svc

    void setUp() throws Exception {
        super.setUp()

        svc = app.bean(WebService)
    }

    @Test
    void test1() throws Exception {
        GspContext ctx = svc.createGspContext();
        ITextBuffer b = ctx.render("data/later/f1.gsp")
        assertEquals(b.toString(), "hello\n" +
                "12end")
    }


}
