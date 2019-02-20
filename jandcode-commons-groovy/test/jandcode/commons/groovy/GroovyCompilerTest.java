package jandcode.commons.groovy;

import jandcode.commons.*;
import org.apache.commons.vfs2.*;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

public class GroovyCompilerTest extends GroovyModuleTestCase {

    public static class Base1 {
        public void run1(String a) {
            System.out.println("A=" + a);
        }

        public void run2(String a) {
            System.out.println("A=" + a);
        }
    }

    @Test
    public void test1() throws Exception {
        utils.logOn();
        GroovyClazz z = getCompiler().getClazz(Base1.class, "void run1(String a)",
                UtFile.getFileObject("res:jandcode/commons/groovy/gdata/test1.txt"), false);
        Base1 x = (Base1) z.createInst();
        x.run1("ss");
        utils.delim("original");
        System.out.println(z.getSourceOriginal());
        utils.delim("clazz");
        System.out.println(z.getSourceClazz());
    }

    @Test
    public void test2() throws Exception {
        utils.logOn();
        GroovyClazz z = getCompiler().getClazz(Base1.class, GroovyCompiler.SIGN_BODY,
                UtFile.getFileObject("res:jandcode/commons/groovy/gdata/test2.txt"), false);
        Base1 x = (Base1) z.createInst();
        x.run1("ss");
        utils.delim("original");
        System.out.println(z.getSourceOriginal());
        utils.delim("clazz");
        System.out.println(z.getSourceClazz());
    }

    @Test
    public void test3() throws Exception {
        utils.logOn();
        GroovyClazz z = getCompiler().getClazz(Base1.class, GroovyCompiler.SIGN_CLASS,
                UtFile.getFileObject("res:jandcode/commons/groovy/gdata/test3.txt"), false);
        Base1 x = (Base1) z.createInst();
        x.run1("ss");
        utils.delim("original");
        System.out.println(z.getSourceOriginal());
        utils.delim("clazz");
        System.out.println(z.getSourceClazz());
    }

    @Test
    public void test_findClass() throws Exception {
        utils.logOn();
        GroovyClazz z1 = getCompiler().getClazz(Base1.class, GroovyCompiler.SIGN_BODY,
                UtFile.getFileObject("res:jandcode/commons/groovy/gdata/test2.txt"), false);
        Base1 x1 = (Base1) z1.createInst();
        //
        GroovyClazz z2 = getCompiler().getClazz(Base1.class, GroovyCompiler.SIGN_BODY,
                UtFile.getFileObject("res:jandcode/commons/groovy/gdata/test2.txt"), false);
        Base1 x2 = (Base1) z2.createInst();
        //
        System.out.println(x1.getClass());
        System.out.println(x2.getClass());
        assertEquals(x1.getClass(), x2.getClass());

        Class c1 = null;
        try {
            c1 = Class.forName(x1.getClass().getName());
            fail();
        } catch (ClassNotFoundException e) {
        }

    }

    @Test
    public void test_useSignatureForClassName_text() throws Exception {
        utils.logOn();
        String scText = "println '1';";
        GroovyClazz z1 = getCompiler().getClazz(Base1.class, "void run1(String a)",
                scText, false);
        GroovyClazz z2 = getCompiler().getClazz(Base1.class, "void run2(String a)",
                scText, false);
        GroovyClazz z3 = getCompiler().getClazz(Base1.class, "void run2(String a)",
                scText, false);

        Base1 x1 = (Base1) z1.createInst();
        Base1 x2 = (Base1) z2.createInst();
        Base1 x3 = (Base1) z3.createInst();

        assertEquals(x1.getClass().getName().equals(x2.getClass().getName()), false);
        assertNotSame(x1.getClass(), x2.getClass());
        assertSame(x3.getClass(), x2.getClass());
    }

    @Test
    public void test_useSignatureForClassName_file() throws Exception {
        utils.logOn();
        FileObject scText = UtFile.getFileObject("res:jandcode/commons/groovy/gdata/test1.txt");
        //
        GroovyClazz z1 = getCompiler().getClazz(Base1.class, "void run1(String a)",
                scText, false);
        GroovyClazz z2 = getCompiler().getClazz(Base1.class, "void run2(String a)",
                scText, false);
        GroovyClazz z3 = getCompiler().getClazz(Base1.class, "void run2(String a)",
                scText, false);

        Base1 x1 = (Base1) z1.createInst();
        Base1 x2 = (Base1) z2.createInst();
        Base1 x3 = (Base1) z3.createInst();

        assertEquals(x1.getClass().getName().equals(x2.getClass().getName()), false);
        assertNotSame(x1.getClass(), x2.getClass());
        assertSame(x3.getClass(), x2.getClass());
    }

}
