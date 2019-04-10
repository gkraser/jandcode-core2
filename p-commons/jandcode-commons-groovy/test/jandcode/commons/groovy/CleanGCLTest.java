package jandcode.commons.groovy;

import groovy.lang.*;
import jandcode.commons.*;
import jandcode.commons.test.*;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

public class CleanGCLTest extends Utils_Test {

    public static class Base1 {
        public void run1(String a) {
            System.out.println("A=" + a);
        }
    }

    @Test
    public void test1() throws Exception {
        Base1 b = new Base1();
        b.run1("aaa");
        //
        GroovyClassLoader gcl = new GroovyClassLoader(getClass().getClassLoader());
        String ctext = "import jandcode.commons.groovy.*;\n" +
                "class My1 extends CleanGCLTest.Base1 { void run1(String a) {println 'a!=1'+a}}";
        Class z = gcl.parseClass(ctext);
        Base1 b2 = (Base1) UtClass.createInst(z);
        b2.run1("fff");
    }

    @Test
    public void test2_fullNameClass() throws Exception {
        GroovyClassLoader gcl = new GroovyClassLoader(getClass().getClassLoader());
        String ctext = "import jandcode.commons.groovy.*;\n" +
                "class a.b.c.My1 extends CleanGCLTest.Base1 { void run1(String a) {println 'a!=1'+a}}";
        Class z = null;
        try {
            z = gcl.parseClass(ctext);
            fail();
        } catch (Exception e) {
        }
    }

    @Test
    public void test2_dollarInClassName() throws Exception {
        GroovyClassLoader gcl = new GroovyClassLoader(getClass().getClassLoader());
        String ctext = "import jandcode.commons.groovy.*;\n" +
                "class a$$b$$c$$My1 extends CleanGCLTest.Base1 { void run1(String a) {println 'a!=1'+a}}";
        Class z = gcl.parseClass(ctext);
        Base1 b2 = (Base1) UtClass.createInst(z);
        b2.run1("fff");
    }

}
