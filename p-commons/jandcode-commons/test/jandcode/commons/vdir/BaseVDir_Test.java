package jandcode.commons.vdir;

import jandcode.commons.*;
import jandcode.commons.test.*;
import org.junit.jupiter.api.*;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public abstract class BaseVDir_Test extends Utils_Test {

    VDir vdir;
    int counter;

    public String toStr(Object v) {
        StringBuilder sb = new StringBuilder();
        if (v instanceof Collection) {
            for (Object x : (Collection) v) {
                if (x instanceof VFile) {
                    VFile z = (VFile) x;
                    sb.append(z.getVirtualPath() + " => " + z.getRealPath());
                } else {
                    sb.append(x);
                }
                sb.append("\n");
            }
        } else {
            sb.append(v);
        }
        String res = sb.toString().trim();
        for (VRoot root : vdir.getRoots()) {
            String nm = UtFile.filename(root.getRootPath());
            String regex = root.getRootPath();
            regex = regex.replaceAll("\\\\", "\\\\\\\\");
            res = res.replaceAll(regex, "[" + nm + "]");
        }
        res = res.replace('\\', '/');
        return res;
    }

    public FsBuilder addRoot(String name) {
        String p = "temp/vdir/";
        if (!UtString.empty(name)) {
            p += "/" + name;
        } else {
            p += counter;
            counter++;
        }
        FsBuilder b = new FsBuilder(p);
        b.clean();
        vdir.addRoot(b.getPath());
        return b;
    }

    public FsBuilder addRoot() {
        return addRoot(null);
    }

    //////

    @Test
    public void test_priority1() throws Exception {
        FsBuilder b;

        //
        b = addRoot();
        b.build(
                b.file("f0.txt"),
                b.file("f1.txt")
        );

        //
        b = addRoot();
        b.build(
                b.file("f1.txt")
        );

        List<VFile> a = vdir.findFiles("");
        assertEquals(toStr(a), "f1.txt => [1]/f1.txt\n" +
                "f0.txt => [0]/f0.txt");
    }

    @Test
    public void test_getVirtuaPath1() throws Exception {
        FsBuilder b;

        //
        b = addRoot();
        b.build(
                b.file("f0.txt")
        );

        String s = vdir.getVirtualPath(b.path("a\\b"));
        assertEquals(s, "a/b");
        s = vdir.getVirtualPath(b.path("a/b"));
        assertEquals(s, "a/b");
    }

    @Test
    public void test_notExistsRoot1() throws Exception {
        vdir.addRoot("W:/t__");
        DirScannerVDir sc = new DirScannerVDir(vdir);
        List<VFile> lst = sc.load();
        assertEquals(lst.size(), 0);
    }

    @Test
    public void test_notExistsRoot2() throws Exception {
        FsBuilder b;
        //
        b = addRoot();
        b.build(
                b.file("f0.txt")
        );
        vdir.addRoot("W:/t__");
        //
        DirScannerVDir sc = new DirScannerVDir(vdir);
        List<VFile> lst = sc.load();
        assertEquals(lst.size(), 1);
    }

}
