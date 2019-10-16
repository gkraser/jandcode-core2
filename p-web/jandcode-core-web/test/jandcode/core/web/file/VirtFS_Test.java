package jandcode.core.web.file;

import jandcode.commons.*;
import jandcode.commons.io.*;
import jandcode.commons.test.*;
import jandcode.core.web.*;
import jandcode.core.web.virtfile.*;
import jandcode.core.web.virtfile.impl.*;
import jandcode.core.web.virtfile.impl.virtfs.*;
import org.apache.commons.vfs2.*;
import org.junit.jupiter.api.*;

import java.util.*;

public class VirtFS_Test extends Utils_Test {

    class DummyTmlCheck implements ITmlCheck {

        public boolean isTml(String ext) {
            return ext.equals("gsp");
        }
    }

    public FsBuilder buildFs1() throws Exception {
        FsBuilder b = new FsBuilder("temp/VirtFS-1");
        b.clean();
        b.build(
                b.dir("d1",
                        b.dir("d3"),
                        b.dir("d4",
                                b.file("z1.txt"),
                                b.file("z2.txt"),
                                b.file("x1.txt.gsp"),
                                b.file("x1.txt.nogsp")
                        ),
                        b.dir("d5"),
                        b.file("f1.txt")
                ),
                b.dir("d2"),
                b.file("f3.txt")
        );
        return b;
    }

    public FsBuilder buildFs2() throws Exception {
        FsBuilder b = new FsBuilder("temp/VirtFS-2");
        b.clean();
        b.build(
                b.dir("d1",
                        b.dir("d3-1"),
                        b.dir("d4",
                                b.file("z1-1.txt"),
                                b.file("z2.txt")
                        )
                ),
                b.file("f2.txt")
        );
        return b;
    }

    /////////

    protected String toPrintable(List<VirtFile> lst, String rootBase) throws Exception {
        if (lst.size() == 0) {
            return "";
        }
        FileObject rf = UtFile.getFileObject(rootBase);
        UtWeb.sortVirtFilesByName(lst);
        StringBuilder sb = new StringBuilder();
        int lenName = 0;
        int lenPath = 0;
        for (VirtFile f : lst) {
            lenName = Math.max(lenName, f.getName().length());
            lenPath = Math.max(lenPath, f.getPath().length());
        }
        for (VirtFile f : lst) {
            if (sb.length() != 0) {
                sb.append("\n");
            }
            sb.append(UtString.padRight(f.getName(), lenName));
            sb.append(" ");
            sb.append(UtString.padRight(f.getPath(), lenPath));
            sb.append(" ");
            sb.append(UtString.padRight("tml?=" + f.isTmlBased(), 12));
            sb.append(UtString.padRight("ft=" + f.getFileType(), 10));
            sb.append(UtString.padRight("ct=" + f.getContentFileType(), 10));

            String fp = "";
            if (f instanceof VirtFileVFS) {
                fp = ((VirtFileVFS) f).getFileObject().toString();
                fp = UtFile.getRelativePath(UtFile.vfsPathToLocalPath(rf.toString()), UtFile.vfsPathToLocalPath(fp));
            } else if (f.isFolder()) {
                fp = "[dir]";
            }
            sb.append(fp);
        }
        return sb.toString();
    }

    @Test
    public void test1() throws Exception {
        FsBuilder b1 = buildFs1();
        FsBuilder b2 = buildFs2();
        //
        VirtFS fs = new VirtFS();
        fs.setTmlCheck(new DummyTmlCheck());
        fs.addMount(new MountVfs("", b1.getPath()));
        fs.addMount(new MountVfs("", b2.getPath()));
        //
        DirScanner<VirtFile> sc = new DirScannerVirtFS(fs, "");
        sc.setNeedDirs(true);
        //
        List<VirtFile> lst = sc.load();
        //
        System.out.println(toPrintable(lst, "temp"));
    }

}
