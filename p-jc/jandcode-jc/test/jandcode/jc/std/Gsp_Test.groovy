package jandcode.jc.std

import jandcode.commons.UtGroovy
import jandcode.commons.groovy.*
import jandcode.jc.*
import org.junit.jupiter.api.*

class Gsp_Test extends CustomProjectTestCase {

    Project p

    @BeforeEach
    public void setUp() throws Exception {
        super.setUp();
        //
        p = load("gsp1")
    }

    @Test
    public void test_compile() throws Exception {
        GspScript g = p.create("jc-data/gsp1.gsp")
        println g
        utils.delim("")
        GroovyClazz gc = UtGroovy.findClazz(g.class)
        utils.delim("source")
        println gc.getSourceOriginal()
        utils.delim("class")
        println gc.getSourceClazz()
        //
    }

    @Test
    public void test_gen1() throws Exception {
        //
        script(p).with {
            GspScript g = p.create("jc-data/gen1.gsp")
            def outDir = wd("temp/gen1")
            ut.cleandir(outDir)
            g.generate("${outDir}/file1.txt")
        }
    }

    @Test
    public void test_out_child1() throws Exception {
        //
        script(p).with {
            GspScript g = p.create("jc-data/gen2-out-child.gsp")
            def outDir = wd("temp/gen2")
            ut.cleandir(outDir)
            g.generate("${outDir}/file1.txt", [a: 1, b: 2])
        }
    }

    @Test
    public void test_funcs() throws Exception {
        //
        script(p).with {
            GspScript g = p.create("jc-data/gen3-func-use1.gsp")
            def outDir = wd("temp/gen3")
            ut.cleandir(outDir)
            g.generate("${outDir}/file1.txt", [a: 1, b: 2])
        }
    }

    @Test
    public void test_resource() throws Exception {
        //
        script(p).with {
            GspScript g = p.create("jc-data/gen4-resource1.gsp")
            def outDir = wd("temp/gen4")
            ut.cleandir(outDir)
            g.generate("${outDir}/file1.txt", [a: 1, b: 2])
        }
    }


}
