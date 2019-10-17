package jandcode.core.jsa.gsp

import jandcode.core.web.test.*
import org.junit.jupiter.api.*

class LinkModule_Test extends Web_Test {

    @Test
    public void test1() throws Exception {
        def s = web.renderGsp("jandcode/core/jsa/gsp/linkModule-1.gsp")
        println s
    }


}
