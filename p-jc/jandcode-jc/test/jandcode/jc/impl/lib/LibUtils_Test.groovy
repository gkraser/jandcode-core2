package jandcode.jc.impl.lib

import jandcode.commons.test.*
import org.junit.jupiter.api.*

import static org.junit.jupiter.api.Assertions.*

class LibUtils_Test extends Utils_Test {

    @Test
    public void test_filenameToLibname() throws Exception {
        assertEquals(LibUtils.filenameToLibname('d:\\path\\lib1.jar'), "lib1")
        assertEquals(LibUtils.filenameToLibname('d:/path/lib1.jar'), "lib1")
        assertEquals(LibUtils.filenameToLibname('d:/path/lib1-lib4.jar'), "lib1-lib4")
        assertEquals(LibUtils.filenameToLibname('d:/path/lib1-lib4-1.0-SNAPSHOT.jar'), "lib1-lib4")
        assertEquals(LibUtils.filenameToLibname('d:/path/lib1-lib4-1.0-SNAPSHOT-src.jar'), "lib1-lib4")
        assertEquals(LibUtils.filenameToLibname('d:\\path\\lib1-src.jar'), "lib1")
        assertEquals(LibUtils.filenameToLibname('d:/path/lib1-src.jar'), "lib1")
        assertEquals(LibUtils.filenameToLibname('d:/path/lib1-lib4-src.jar'), "lib1-lib4")
        assertEquals(LibUtils.filenameToLibname('d:/path/lib1-lib4-source.jar'), "lib1-lib4")
        assertEquals(LibUtils.filenameToLibname('d:/path/lib1-lib4-sources.jar'), "lib1-lib4")
    }

    @Test
    public void test_findJarFileByResource() throws Exception {
        String s
        s = LibUtils.findJarFileByResource("org/junit/runners/AllTests.class")
        println s
        s = LibUtils.findJarFileByResource("jandcode/jc/Project.class")
        println s
    }

    @Test
    public void test_findJarFileByClassname() throws Exception {
        String s
        s = LibUtils.findJarFileByClassname("org.junit.runners.AllTests")
        println s
        s = LibUtils.findJarFileByClassname("Project")
        println s
    }

    @Test
    public void test_extractVersion() throws Exception {
        assertEquals(LibUtils.extractVersion("jquery-2.1.4.min.js"), "2.1.4")
        assertEquals(LibUtils.extractVersion("jquery-21.4.min.js"), "21.4")
        assertEquals(LibUtils.extractVersion("jquery-214.min.js"), "214")
        assertEquals(LibUtils.extractVersion("jquery-2-1.min.js"), "2")
        assertEquals(LibUtils.extractVersion("jquery-.min.js"), null)
    }


}
