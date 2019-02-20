package jandcode.commons;

import jandcode.commons.test.*;
import org.junit.jupiter.api.*;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class UtClass_Test extends Utils_Test {

    class C1 {
    }

    class C2 extends C1 {
    }

    class C3 extends C2 {
    }

    @Test
    public void test_isPublic() throws Exception {
        assertEquals(UtClass.isPublic(getClass()), true);
    }

    @Test
    public void test_getInheritedList() throws Exception {
        List<Class> res = UtClass.getInheritedList(C1.class, C3.class);
        assertEquals(res.size(), 3);
        assertEquals(res.get(0), C1.class);
        assertEquals(res.get(2), C3.class);
        //
        res = UtClass.getInheritedList(C1.class, C2.class);
        assertEquals(res.size(), 2);
        assertEquals(res.get(0), C1.class);
        assertEquals(res.get(1), C2.class);
        //
        res = UtClass.getInheritedList(C1.class, C1.class);
        assertEquals(res.size(), 1);
        assertEquals(res.get(0), C1.class);
    }

    @Test
    public void test_getClass() throws Exception {
        assertEquals(UtClass.getClass("int"), int.class);
    }

    @Test
    public void test_findClassesInPackage() throws Exception {
        List<Class> res = UtClass.findClassesInPackage("jandcode.commons", null);

    }

    @Test
    public void test_getClasspath() throws Exception {
        UtClass.addClasspath(UtFile.getWorkdir());
        List<String> z = UtClass.getClasspath();
        System.out.println(z);
    }


}
