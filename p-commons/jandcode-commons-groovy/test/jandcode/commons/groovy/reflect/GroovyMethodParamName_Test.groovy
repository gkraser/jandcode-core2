package jandcode.commons.groovy.reflect

import jandcode.commons.test.*
import org.junit.jupiter.api.*

import java.lang.reflect.*

import static org.junit.jupiter.api.Assertions.*

public class GroovyMethodParamName_Test extends Utils_Test {

    public static class Exam1 {

        public void m1(String param1, String pId2) {
        }

    }

    @Test
    public void test1() throws Exception {
        Method[] mth = Exam1.class.getDeclaredMethods();
        Method m = mth[0];
        System.out.println(m);
        Parameter[] prms = m.getParameters();
        System.out.println(prms[0].getName() + ":" + prms[0].isNamePresent());
        System.out.println(prms[1].getName() + ":" + prms[1].isNamePresent());
        assertTrue(prms[0].isNamePresent(), "no parameter names, not use -parameters option in javac");
        assertTrue(prms[1].isNamePresent(), "no parameter names, not use -parameters option in javac");
        assertEquals(prms[0].getName(), "param1");
        assertEquals(prms[1].getName(), "pId2");
    }


}
