package jandcode.commons;

import jandcode.commons.test.*;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

public class UtVDir_Test extends Utils_Test {

    @Test
    public void test_isRelPath() throws Exception {
        assertEquals(UtVDir.isRelPath(""), false);
        assertEquals(UtVDir.isRelPath("s"), false);
        assertEquals(UtVDir.isRelPath("s/../s"), true);
        assertEquals(UtVDir.isRelPath("./s"), true);
    }

    @Test
    public void test_expandRelPath() throws Exception {
        assertEquals(UtVDir.expandRelPath("", "../sss"), "");
        assertEquals(UtVDir.expandRelPath("", "sss////ddd"), "sss/ddd");
        assertEquals(UtVDir.expandRelPath("as", "sss////ddd"), "sss/ddd");
        assertEquals(UtVDir.expandRelPath("a/b/c", "./d"), "a/b/c/d");
        assertEquals(UtVDir.expandRelPath("a/b/c", "../d"), "a/b/d");
    }

    @Test
    public void test_getRelPath() throws Exception {
        assertEquals(UtVDir.getRelPath("a/b/c", "a/b/c/d/e"), "d/e");
        assertEquals(UtVDir.getRelPath("a/b/c", "a/b/c/d/"), "d");
        assertEquals(UtVDir.getRelPath("a/b/c", "a/b/d/"), "../d");
    }

    //////

    private void matchPath(String mask, String path, boolean exp) {
        boolean b = UtVDir.matchPath(mask, path);
        assertEquals(b, exp);
    }

    @Test
    public void matchPath_1() throws Exception {
        matchPath("*.xx", "file.xx", true);
        matchPath("*.xx", "asd/file.xx", false);
        matchPath("*", "asd/file.xx", false);
        matchPath("*.*", "asd/file.xx", false);
        matchPath("*.x1", "asd/file.xx", false);
        matchPath("*.", "asd/file.xx", false);
        matchPath("*.", "asd/file", false);
        //
        matchPath("test/*.xx", "file/test/file.xx", false);
        matchPath("test/*.xx", "file/1test/file.xx", false);
        //
        matchPath("test/*/index.xx", "test/path1/index.xx", true);
        matchPath("*/index.xx", "path1/index.xx", true);

    }


    @Test
    public void matchPath_2() throws Exception {
        // from ant Pattern documentation

        String m;

        m = "**/CVS/*";
        matchPath(m, "CVS/Repository", true);
        matchPath(m, "org/apache/CVS/Entries", true);
        matchPath(m, "org/apache/jakarta/tools/ant/CVS/Entries", true);
        matchPath(m, "org/apache/CVS/foo/bar/Entries", false);

        m = "org/apache/jakarta/**";
        matchPath(m, "org/apache/jakarta/tools/ant/docs/index.html", true);
        matchPath(m, "org/apache/jakarta/test.xml", true);
        matchPath(m, "org/apache/xyz.java", false);

        m = "org/apache/**/CVS/*";
        matchPath(m, "org/apache/CVS/Entries", true);
        matchPath(m, "org/apache/jakarta/tools/ant/CVS/Entries", true);
        matchPath(m, "org/apache/CVS/foo/bar/Entries", false);

    }


}
