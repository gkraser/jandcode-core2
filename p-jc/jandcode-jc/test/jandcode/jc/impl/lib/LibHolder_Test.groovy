package jandcode.jc.impl.lib


import jandcode.jc.*
import org.junit.jupiter.api.*

import static org.junit.jupiter.api.Assertions.*

class LibHolder_Test extends CustomProjectTestCase {

    @Test
    public void test_1() throws Exception {
        prepareRealCtx()
        String root1 = getJcAppdir()
        ctx.load(root1)
        Project p = load("workdir1")
        def lst = p.ctx.getLibs("jandcode-jc")
        def lst2 = p.ctx.getLibs(lst)
        assertEquals(lst.toString(), lst2.toString())
    }

}
