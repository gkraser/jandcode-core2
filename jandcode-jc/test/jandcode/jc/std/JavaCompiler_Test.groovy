package jandcode.jc.std

import jandcode.jc.*
import org.junit.jupiter.api.*

class JavaCompiler_Test extends CustomProjectTestCase {

    @Test
    public void test_1() throws Exception {
        ctx.log.verbose = true
        prepareRealCtx()
        //
        Project p = load("workdir1")
        script(p).with {
            def c = include(JavaCompiler.class)
            c.compile(libs: ['groovy'])
        }
    }


}
