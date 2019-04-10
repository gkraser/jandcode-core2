package jandcode.jc.impl

import jandcode.jc.*
import org.junit.jupiter.api.*

import java.util.regex.*

class ScriptHolder_Test extends CustomProjectTestCase {

    @Test
    public void test_patternBeforeLoad() throws Exception {
        def s = """
        h static beforeLoad= {xxx}}}

        """
        Pattern P_BEFORELOAD = Pattern.compile("static\\s+beforeLoad\\s*=\\s*(\\{.*)", Pattern.DOTALL);
        def m = P_BEFORELOAD.matcher(s)
        println m.find()

        println m.group(1)

    }

    @Test
    public void test_extractBeforeLoad() throws Exception {
        def p = basepath("beforeload1/p1.jc")
        println p
        String c = ctx.getBeforeLoadProjectScript(p)
        println c
        def r = ctx.getClassProjectScript(c, null)
        println r
    }

    @Test
    public void test_load1() throws Exception {
        def p = load("beforeload1/p1.jc")
    }


}
