package jandcode.commons.version;

import jandcode.commons.*;
import jandcode.commons.test.*;
import jandcode.commons.version.data.*;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

public class VersionInfo_Test extends Utils_Test {

    @Test
    public void test1() throws Exception {
        VersionInfo vi = new VersionInfo("jandcode.commons.version.data");
        assertEquals(vi.getVersion(), "6");
    }

    @Test
    public void test2() throws Exception {
        VersionInfo vi = new VersionInfo("jandcode.commons.version.data.x.t.y.w");
        assertEquals(vi.getVersion(), "6");
    }

    @Test
    public void test3() throws Exception {
        VersionInfo vi = new VersionInfo("a.b.d.s.r");
        assertEquals(vi.getVersion(), VersionInfo.DEFAULT_VERSION);
    }

    @Test
    public void test4() throws Exception {
        assertEquals(UtVersion.getVersion(Dummy1.class), "6");
    }

    @Test
    public void buildDate() throws Exception {
        VersionInfo vi = new VersionInfo("jandcode.commons.version.data.x.t.y.w");
        System.out.println(vi.getBuildDate());
    }

}
