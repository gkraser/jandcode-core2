package jandcode.core.web.filter

import jandcode.core.web.test.*
import org.junit.jupiter.api.*

class Filter_Test extends Web_Test {

    @Test
    public void test1() throws Exception {
        web.execRequest("/")
    }

    @Test
    public void test2_error() throws Exception {
        try {
            web.execRequest("/a1")
        } catch (e) {
            utils.showError(e)
        }
    }


}
