package jandcode.web.file;

import jandcode.commons.*;
import jandcode.commons.test.*;
import org.apache.commons.vfs2.*;
import org.junit.jupiter.api.*;

import java.io.*;

import static org.junit.jupiter.api.Assertions.*;

public class FileObject_Test extends Utils_Test {

    public FsBuilder buildFs1() throws Exception {
        FsBuilder b = new FsBuilder("temp/fs-fileobject1");
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

    @Test
    public void test1() throws Exception {
        FsBuilder b = buildFs1();

        FileObject foup = UtFile.getFileObject(b.getPath());
        long tu1 = foup.getContent().getLastModifiedTime();

        // при добавлении файла менятся время модификации папки
        String p = b.path("d1");
        FileObject fo = UtFile.getFileObject(p);
        long t1 = fo.getContent().getLastModifiedTime();
        //
        String p2 = b.path("d1/f2.txt");
        UtFile.saveString("111", new File(p2));
        //
        long t2 = fo.getContent().getLastModifiedTime();
        assertTrue(t1 != t2);

        Thread.sleep(150);
        // при изменении файла время модификации папки не меняется
        UtFile.saveString("222", new File(p2));
        long t3 = fo.getContent().getLastModifiedTime();
        assertTrue(t2 == t3);

        // при удалении папки время изменяется
        File f = new File(p2);
        f.delete();

        long t4 = fo.getContent().getLastModifiedTime();
        assertTrue(t3 != t4);

        // пупку выше уровнем эти манипуляции никак не затрагивают
        long tu4 = foup.getContent().getLastModifiedTime();
        assertTrue(tu1 == tu4);

    }

    @Test
    public void test_isJar() throws Exception {
        FileObject f = UtFile.getFileObject("res:org/junit/jupiter/api/AfterEach.class");
        System.out.println(f);
        System.out.println(f.getFileSystem().getRootURI());

    }


}
