package jandcode.jc.nodejs

import jandcode.jc.nodejs.impl.*
import jandcode.jc.test.*
import org.junit.jupiter.api.*

class NodeJsModuleLoader_Test extends JcTestProjectsTestCase {

    @Test
    @Disabled
    public void test1() throws Exception {
        def dir = "W:\\p\\jandcode-core2-ws-git\\jandcode-core2-sandbox\\projects\\sandbox-apx2\\node_modules"
        def ldr = new NodeJsModuleLoader(dir)
        stopwatch.start()
        def lst = ldr.load()
        stopwatch.stop()
        println lst.size()
        println lst
    }

    @Test
    @Disabled
    public void test2() throws Exception {
        def dir = "W:\\p\\jandcode-core2-ws-git\\jandcode-core2-sandbox\\projects\\sandbox-apx2\\node_modules\\@f*\\*"
        def ldr = new NodeJsModuleLoader(dir)
        stopwatch.start()
        def lst = ldr.load()
        stopwatch.stop()
        println lst.size()
        for (a in lst) {
            println a
        }
    }


}
