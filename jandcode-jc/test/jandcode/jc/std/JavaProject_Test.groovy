package jandcode.jc.std

import jandcode.jc.*
import org.junit.jupiter.api.*

class JavaProject_Test extends CustomProjectTestCase {

    Project p

    void setUp() throws Exception {
        super.setUp()
        //
        ctx.log.verbose = true
        prepareRealCtx()
        //
        p = load("workdir1")
        script(p).with {
            def jm = include(JavaProject)
            jm.packageRoot = "pak1"
            jm.depends.prod.add("groovy", "commons-io")
            jm.depends.dev.add(
                    'junit-jupiter-api',
                    'junit-jupiter-params',
                    'junit-jupiter-engine',
                    'junit-platform-launcher',
            )
        }
    }

    @Test
    public void test_clean() throws Exception {
        help(p)
        p.cm.exec("clean")
    }

    @Test
    public void test_isCompiled() throws Exception {
        script(p).with {
            def jm = p.include(JavaProject)
            println "COMP=" + jm.isCompiledClasses()
            jm.compileClasses()
            println "COMP=" + jm.isCompiledClasses()
            jm.cm.exec("clean")
            println "COMP=" + jm.isCompiledClasses()
            jm.compileClasses()
        }
    }

    @Test
    public void test_showlibs_help() throws Exception {
        help(p, "showlibs")
    }

    @Test
    public void test_test() throws Exception {
        p.cm.exec("test")
    }

    @Test
    public void test_showinfo() throws Exception {
        p.cm.exec("showinfo")
    }

}
