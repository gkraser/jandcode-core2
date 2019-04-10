package jandcode.jc.impl.lib

import jandcode.jc.*
import org.junit.jupiter.api.*

class LibDirLoader_Test extends CustomProjectTestCase {

    @Test
    public void test_1() throws Exception {
        Project p = load("workdir1")
        String libdir = p.wd("libdir1")
        LibDirLoader ldr = new LibDirLoader(ctx, libdir)
        List lst = ldr.load()
        for (z in lst) {
            println z
            println "  [jar] ${z.jar}"
            println "  [src] ${z.src}"
        }
    }

}
