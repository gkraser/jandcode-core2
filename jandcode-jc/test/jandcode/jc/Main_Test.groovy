package jandcode.jc

import groovy.transform.*
import org.junit.jupiter.api.*

@CompileStatic
class Main_Test extends CustomProjectTestCase {

    @Test
    public void test_no_command() throws Exception {
        run("main1", [])
    }

    @Test
    public void test_hello_h() throws Exception {
        run("main1", ['hello', '-h'])
    }

    @Test
    public void test_hello() throws Exception {
        run("main1", ['hello'])
    }


}
