package jandcode.jc.impl

import jandcode.jc.*
import org.junit.jupiter.api.*

class InternalStatic_Test extends CustomProjectTestCase {

    @Test
    public void test1() throws Exception {
        Project p
        p = load("internal_static1")
    }

}
