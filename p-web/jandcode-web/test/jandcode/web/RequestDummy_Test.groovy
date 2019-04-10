package jandcode.web

import jandcode.web.impl.*
import jandcode.web.test.*
import org.junit.jupiter.api.*

class RequestDummy_Test extends Web_Test {

    @Test
    public void test_1() throws Exception {
        ((WebServiceImpl) web.getWebService()).setRequest(null);

        println web.getRequest()
        println web.getRequest().ref("a/b")
    }


}
