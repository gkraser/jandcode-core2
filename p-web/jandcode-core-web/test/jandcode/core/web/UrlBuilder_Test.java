package jandcode.core.web;

import jandcode.commons.test.*;
import org.junit.jupiter.api.*;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class UrlBuilder_Test extends Utils_Test {

    public UrlBuilder z;

    public void setUp() throws Exception {
        super.setUp();
        z = new UrlBuilder();
    }

    @Test
    public void test0() throws Exception {
        assertEquals(z.toString(), "");
    }


    @Test
    public void test1() throws Exception {
        z.append("context");
        z.append("ctrl");
        z.append("act");
        z.append("p1", "v1");
        z.append("p2", "v2");
        assertEquals(z.toString(), "context/ctrl/act?p1=v1&p2=v2");
    }

    @Test
    public void test2() throws Exception {
        z.append("http://q/d/");
        z.append("/ctrl/dd/ff");
        z.append("act/");
        assertEquals(z.toString(), "http://q/d/ctrl/dd/ff/act");
    }

    @Test
    public void test3() throws Exception {
        z.append("http://q?a=1");
        z.append("ctrl?b=4&e=5");
        z.append("act/");
        assertEquals(z.toString(), "http://q/ctrl/act?a=1&b=4&e=5");
    }

    @Test
    public void test4() throws Exception {
        z.append("c/a");
        z.append("b=4&e=5");
        z.append("c=6");
        assertEquals(z.toString(), "c/a?b=4&e=5&c=6");
    }

    @Test
    public void test5() throws Exception {
        z.append("c/a");
        z.append("b=4&e=5");
        Map p = new HashMap();
        p.put("pp1", 22);
        p.put("pp2", 33);
        p.put("pp3", "");
        p.put("pp4", null);
        z.append(p);
        z.append("c=привет");
        assertEquals(z.toString(), "c/a?b=4&e=5&pp1=22&pp3&pp2=33&pp4&c=%D0%BF%D1%80%D0%B8%D0%B2%D0%B5%D1%82");
    }

    @Test
    public void test6() throws Exception {
        z.append("c/a?1");
        assertEquals(z.toString(), "c/a?1");
    }


    @Test
    public void testRoot1() throws Exception {
        z.append("/context");
        z.append("ctrl");
        z.setRoot(true);
        assertEquals(z.toString(), "/context/ctrl");
    }

    @Test
    public void testRoot2() throws Exception {
        z.append("/");
        z.setRoot(true);
        assertEquals(z.toString(), "/");
    }

    @Test
    public void testRoot3() throws Exception {
        z.append("/");
        assertEquals(z.toString(), "");
    }

    @Test
    public void testSlash1() throws Exception {
        z.append("c/a");
        z.append("b=4&e=a/5");
        Map p = new HashMap();
        p.put("pp1", 22);
        p.put("pp2", "a/v/d/");
        z.append(p);
        assertEquals(z.toString(), "c/a?b=4&e=a/5&pp1=22&pp2=a/v/d/");
    }

}
