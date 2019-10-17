package jandcode.core.web.file;

import jandcode.commons.test.*;
import jandcode.core.web.virtfile.impl.virtfs.*;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

public class PrefixVsPath_Test extends Utils_Test {

    @Test
    public void test1() throws Exception {
        PrefixVsPath p;

        //
        p = new PrefixVsPath("a", "a");
        assertEquals(p.getRel(), PrefixVsPath.REL_EQUAL);
        assertEquals(p.getVirtualPath(), "a");
        //
        p = new PrefixVsPath("a/b", "a/b");
        assertEquals(p.getRel(), PrefixVsPath.REL_EQUAL);
        assertEquals(p.getVirtualPath(), "b");

        //
        p = new PrefixVsPath("a", "a/b");
        assertEquals(p.getRel(), PrefixVsPath.REL_PATH_STARTSWITH_PREFIX);
        assertEquals(p.getVirtualPath(), "b");
        //
        p = new PrefixVsPath("a", "a/b/c");
        assertEquals(p.getRel(), PrefixVsPath.REL_PATH_STARTSWITH_PREFIX);
        assertEquals(p.getVirtualPath(), "b/c");

        //
        p = new PrefixVsPath("a/b", "a");
        assertEquals(p.getRel(), PrefixVsPath.REL_PREFIX_STARTSWITH_PATH);
        assertEquals(p.getVirtualPath(), "a");
        assertEquals(p.getVirtualPath2(), "b");
        //
        p = new PrefixVsPath("a/b/c", "a/b");
        assertEquals(p.getRel(), PrefixVsPath.REL_PREFIX_STARTSWITH_PATH);
        assertEquals(p.getVirtualPath(), "b");
        assertEquals(p.getVirtualPath2(), "c");
        //
        p = new PrefixVsPath("a/b/c/d/e", "a/b");
        assertEquals(p.getRel(), PrefixVsPath.REL_PREFIX_STARTSWITH_PATH);
        assertEquals(p.getVirtualPath(), "b");
        assertEquals(p.getVirtualPath2(), "c");

        //
        p = new PrefixVsPath("a/b/c/d/e", "");
        assertEquals(p.getRel(), PrefixVsPath.REL_PATH_EMPTY);
        assertEquals(p.getVirtualPath(), "a");
        //

    }


}
