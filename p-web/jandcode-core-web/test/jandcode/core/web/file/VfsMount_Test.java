package jandcode.core.web.file;

import jandcode.commons.test.*;
import jandcode.core.web.*;
import jandcode.core.web.virtfile.*;
import jandcode.core.web.virtfile.impl.virtfs.*;
import org.junit.jupiter.api.*;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class VfsMount_Test extends Utils_Test {

    MountVfs p;

    public void setUp() throws Exception {
        super.setUp();
        //
        p = new MountVfs();
    }

    public FsBuilder buildFs1() throws Exception {
        FsBuilder b = new FsBuilder("temp/fs-MountPointVFS_Test-1");
        b.clean();
        b.build(
                b.dir("d1",
                        b.dir("d3"),
                        b.dir("d4",
                                b.dir("z1"),
                                b.dir("z2")
                        ),
                        b.dir("d5"),
                        b.file("f1.txt")
                ),
                b.dir("d2"),
                b.file("f3.txt")
        );
        return b;
    }

    private void check(String folder, boolean valid, boolean exist, String files) {
        System.out.println(folder + "=>");
        FolderContent f = p.getFolderContent(folder);
        assertEquals(valid, f.isValid(), "valid");
        assertEquals(exist, f.isExists(), "exist");
        List<VirtFile> lst = f.getFiles();
        UtWeb.sortVirtFilesByName(lst);
        String s = lst.toString();
        System.out.println(s);
        assertEquals(files, s, "files");
    }

    @Test
    public void test_findFiles_noPrefix() throws Exception {
        FsBuilder b = buildFs1();
        //
        p.setRealPath(b.getPath());

        check("ooo", true, false, "[]");
        check("", true, true, "[d1, d2, f3.txt]");
        check("d1", true, true, "[d1/d3, d1/d4, d1/d5, d1/f1.txt]");
        check("d1/d4", true, true, "[d1/d4/z1, d1/d4/z2]");
    }

    @Test
    public void test_findFiles_prefix() throws Exception {
        FsBuilder b = buildFs1();
        //
        p.setRealPath(b.getPath());
        p.setVirtualPath("a/b/c");

        check("ooo", false, false, "[]");
        check("ooo/vvv", false, false, "[]");
        check("", true, true, "[a]");
        check("a", true, true, "[a/b]");
        check("a/b", true, true, "[a/b/c]");

        check("a/b/c", true, true, "[a/b/c/d1, a/b/c/d2, a/b/c/f3.txt]");
        check("a/b/c/d1", true, true, "[a/b/c/d1/d3, a/b/c/d1/d4, a/b/c/d1/d5, a/b/c/d1/f1.txt]");
        check("a/b/c/d1/d4", true, true, "[a/b/c/d1/d4/z1, a/b/c/d1/d4/z2]");

        check("a/vvv", false, false, "[]");
        check("a/b/vvv", false, false, "[]");
        check("a/b/c/vvv", true, false, "[]");

    }


}
