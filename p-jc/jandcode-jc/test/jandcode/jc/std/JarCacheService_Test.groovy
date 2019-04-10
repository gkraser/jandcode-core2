package jandcode.jc.std

import jandcode.jc.*
import org.junit.jupiter.api.*

class JarCacheService_Test extends CustomProjectTestCase {

    Project p

    @BeforeEach
    public void setUp() throws Exception {
        super.setUp();
        //
        p = load("workdir1")
        prepareRealCtx()
    }

    @Test
    public void test1() throws Exception {
        Lib x = p.ctx.getLib("commons-io");
        String s = ctx.service(JarCacheService).getMetaInfDir(x.jar)
        println s
        s = ctx.service(JarCacheService).getContentDir(x.jar)
        println s
    }


}
