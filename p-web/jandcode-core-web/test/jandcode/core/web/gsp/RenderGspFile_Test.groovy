package jandcode.core.web.gsp

import jandcode.core.web.test.*
import org.junit.jupiter.api.*

import static org.junit.jupiter.api.Assertions.*

class RenderGspFile_Test extends Web_Test {

    @Test
    public void test_css() throws Exception {
        String s = web.execRequest("data/f3.css")
        assertEquals(s, """\
a {
  height: 10px
}""")
    }

    @Test
    public void test_js() throws Exception {
        String s = web.execRequest("data/f4.js")
        assertEquals(s, """\
var a = 1;""")
    }

}
