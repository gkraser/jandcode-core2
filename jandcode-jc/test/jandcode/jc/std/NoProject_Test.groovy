package jandcode.jc.std

import jandcode.jc.*
import org.junit.jupiter.api.*

class NoProject_Test extends CustomProjectTestCase {

    @Test
    public void test1() throws Exception {
        run("workdir1", [])
        //

    }

    @Test
    public void test2() throws Exception {
        try {
            run("workdir1", ['create', '-h', '-t', '-v'])
        } catch (ignore) {
        }
    }


}
