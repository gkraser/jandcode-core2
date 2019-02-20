package jandcode.jc.impl.just

import jandcode.commons.test.*
import jandcode.jc.*
import org.junit.jupiter.api.*

import static org.junit.jupiter.api.Assertions.*

class Dir_Test extends Utils_Test {

    @Test
    public void test1() throws Exception {
        Dir wd = new DirImpl("m:/temp");

        assertEquals("" + wd, "m:\\temp")
        assertEquals(wd.name, "temp")
        assertEquals(wd('ddd'), "m:\\temp\\ddd")
        assertEquals(wd('n:/ttt'), "n:\\ttt")
    }


}
