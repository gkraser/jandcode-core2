package jandcode.jsa.gsp

import jandcode.core.web.test.Web_Test
import org.junit.jupiter.api.*

class LinkModule_Test extends Web_Test {

    @Test
    public void test1() throws Exception {
        def s = web.renderGsp("jandcode/jsa/gsp/linkModule-1.gsp")
        println s
    }


}
