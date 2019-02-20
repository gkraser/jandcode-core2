package jandcode.jc.impl.cm

import jandcode.jc.*
import org.junit.jupiter.api.*

import static org.junit.jupiter.api.Assertions.*

class Cm_Test extends CustomProjectTestCase {

    @Test
    public void test_sysOptions() throws Exception {
        Project p = load("workdir1")
        //
        p.cm.opt("ff", 1, "z")
        try {
            p.cm.opt("h", 1, "z")  //SYS!
            fail()
        } catch (ignored) {
        }
    }

}
