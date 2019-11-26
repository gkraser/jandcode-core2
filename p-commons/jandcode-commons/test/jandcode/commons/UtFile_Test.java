package jandcode.commons;

import jandcode.commons.test.*;
import org.apache.commons.vfs2.*;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

public class UtFile_Test extends Utils_Test {

    @Test
    public void test_getRelativePath() throws Exception {
        assertEquals(UtFile.getRelativePath(".", ".."), "..");
        assertEquals(UtFile.getRelativePath(".", "./src/jandcode"), "src/jandcode");
        assertEquals(UtFile.getRelativePath("./src/jandcode", "."), "../..");
    }

    @Test
    public void test_isAbsolute() throws Exception {
        assertEquals(UtFile.isAbsolute(utils.getTestFile("UtFile_Test.class")), true);
        assertEquals(UtFile.isAbsolute("res:jandcode/aaa/bbb.xml"), true);
        assertEquals(UtFile.isAbsolute("c:\\dd"), true);
        assertEquals(UtFile.isAbsolute("c:\\dd\\"), true);
        assertEquals(UtFile.isAbsolute("c:/dd/"), true);
        assertEquals(UtFile.isAbsolute("c:/dd"), true);
        assertEquals(UtFile.isAbsolute("dd/sss/s.txt"), false);
        assertEquals(UtFile.isAbsolute("dd\\sss\\s.txt"), false);
        assertEquals(UtFile.isAbsolute("/dd/sss/s.txt"), false);
        assertEquals(UtFile.isAbsolute("\\dd\\sss\\s.txt"), false);
        assertEquals(UtFile.isAbsolute("\\\\dd\\sss\\s.txt"), true);
        assertEquals(UtFile.isAbsolute("c:dd/"), false);
        assertEquals(UtFile.isAbsolute("c:dd"), false);
        assertEquals(UtFile.isAbsolute("jar:file:///d:/p/blabla/lala/jandcode-core-dbm/temp/lib/jandcode-core-dbm.jar!/jandcode/dbm/model.cfx"), true);
    }

    @Test
    public void test_basename() throws Exception {
        String s = "$P$/../../ja/m/je.iml";
        assertEquals(UtFile.basename(s), "je");
    }

    @Test
    public void test_abs() throws Exception {
        assertNotNull(UtFile.abs("sd/d.xx1"));
    }

    @Test
    public void test_absCanonical() throws Exception {
        assertNotNull(UtFile.absCanonical("d:\\temp//sd/../d.xx1"));
    }

    @Test
    public void test_unnormPath() throws Exception {
        assertEquals(UtFile.unnormPath("asd/"), "asd");
        assertEquals(UtFile.unnormPath("asd\\"), "asd");
        assertEquals(UtFile.unnormPath("asd/\\"), "asd");
        assertEquals(UtFile.unnormPath("asd\\///"), "asd");
        assertEquals(UtFile.unnormPath("a/b/c"), "a/b/c");
        assertEquals(UtFile.unnormPath("a/b/c/"), "a/b/c");
    }

    @Test
    public void testFindFileUp() throws Exception {
        UtFile.mkdirs("temp/FindFileUp/FindFileUp2/FindFileUp3");
        String s = UtFile.findFileUp("FindFileUp", "temp/FindFileUp/FindFileUp2/FindFileUp3");
        assertEquals(s.endsWith("temp\\FindFileUp"), true);
        s = UtFile.findFileUp("xxx__1", "temp/FindFileUp/FindFileUp2/FindFileUp3");
        assertEquals(s, null);
    }

    @Test
    public void test_removeExt() throws Exception {
        assertEquals(UtFile.removeExt("aaa.bbb"), "aaa");
        assertEquals(UtFile.removeExt("aaa.bbb.ccc"), "aaa.bbb");
    }

    @Test
    public void test_join() throws Exception {
        assertEquals(UtFile.join("aa", "bb"), "aa\\bb");
        assertEquals(UtFile.join("aa", "bb", "cc"), "aa\\bb\\cc");
    }

    @Test
    public void test_relativePath() throws Exception {
        String bp = utils.getTestPath();
        String absDir = bp + "temp\\aa\\bb";
        String absFile = bp + "temp\\aa\\bb\\cc\\dd.txt";
        assertEquals(UtFile.getRelativePath(absDir, absFile), "cc/dd.txt");

        absFile = bp + "temp\\cc\\bb\\cc\\dd.txt";
        assertEquals(UtFile.getRelativePath(absDir, absFile), "../../cc/bb/cc/dd.txt");

    }

    @Test
    public void test_relativePath_VFS() throws Exception {
        String bp = utils.getTestPath();
        FileObject absDir = UtFile.getFileObject(bp + "temp\\aa\\bb");
        FileObject absFile = UtFile.getFileObject(bp + "temp\\aa\\bb\\cc\\dd.txt");
        assertEquals(absDir.getName().getRelativeName(absFile.getName()), "cc/dd.txt");

        absFile = UtFile.getFileObject(bp + "temp\\cc\\bb\\cc\\dd.txt");
        assertEquals(absDir.getName().getRelativeName(absFile.getName()), "../../cc/bb/cc/dd.txt");
    }

    @Test
    public void test_fileName() throws Exception {
        assertEquals(UtFile.filename("aa/bb.txt"), "bb.txt");
        assertEquals(UtFile.filename("bb.txt"), "bb.txt");
        assertEquals(UtFile.filename("/bb.txt"), "bb.txt");
    }

    @Test
    public void test_path() throws Exception {
        assertEquals(UtFile.path("aa/bb.txt"), "aa");
        assertEquals(UtFile.path("bb.txt"), "");
        assertEquals(UtFile.path("/bb.txt"), "");
    }

    private void check_splitPathAndMask(String path, String p1, String mask) {
        String[] m = UtFile.splitPathAndMask(path, "~");
        assertEquals(m[0], p1);
        if (mask == null) {
            assertEquals(m[1], "~");
        } else {
            assertEquals(m[1], mask);
        }
    }

    @Test
    public void test_splitPathAndMask() throws Exception {
        check_splitPathAndMask("d:\\t", "d:\\t", null);
        check_splitPathAndMask("d:/t", "d:/t", null);
        check_splitPathAndMask("d:/t\\w", "d:/t\\w", null);
        check_splitPathAndMask("d:/t\\*.cfx", "d:/t", "*.cfx");
        check_splitPathAndMask("d:/t\\*?.cfx", "d:/t", "*?.cfx");
        check_splitPathAndMask("d:/t/*?.cfx", "d:/t", "*?.cfx");
        check_splitPathAndMask("*?.cfx", "", "*?.cfx");
        check_splitPathAndMask("", "", null);
    }


}
