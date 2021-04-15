package jandcode.core.nodejs.jc

import jandcode.commons.test.*
import jandcode.core.nodejs.jc.impl.*
import org.junit.jupiter.api.*

import static org.junit.jupiter.api.Assertions.*;

class NodeJsModule_Test extends Utils_Test {

    @Test
    public void test1() throws Exception {
        NodeJsModule m = new NodeJsModuleImpl(utils.getTestFile("data/package1.json"))
        assertEquals(m.getName(), "vinyl")
        assertEquals(m.getVersion(), "2.2.1")
        //
        println m.getDependencies()
        println m.getDevDependencies()
        println m.getAllDependencies()
    }

}
