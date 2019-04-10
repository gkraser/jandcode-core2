package jandcode.commons;

import jandcode.commons.test.*;
import org.junit.jupiter.api.*;

import java.net.*;
import java.util.*;

public class UtLang_Test extends Utils_Test {

    @Test
    public void test1() throws Exception {
        Enumeration<URL> en = getClass().getClassLoader().getResources("META-INF/langres.xml");
        while (en.hasMoreElements()) {
            URL u = en.nextElement();
            System.out.println(u);
        }
    }


}
