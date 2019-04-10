package jandcode.jc.impl.just

import jandcode.jc.*
import org.junit.jupiter.api.*

import static org.junit.jupiter.api.Assertions.*

class Vars_Test extends CustomProjectTestCase {

    @Test
    public void test_Log1() throws Exception {
        Vars v = new VarsImpl()
        //
        v.prop1 = "123"
        println v.prop1
        println v.prop2  // null, наличие не проверяется
        v.m1 = {
            println "M!"
        }
        println v.m1
        println v.m1()
        try {
            v.m0()
            fail()       // closure = null, ошибка
        } catch (ignore) {
        }
    }

}
