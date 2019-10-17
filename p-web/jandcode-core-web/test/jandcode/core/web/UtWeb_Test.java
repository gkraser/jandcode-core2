package jandcode.core.web;

import jandcode.commons.*;
import jandcode.commons.datetime.*;
import jandcode.core.test.*;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

public class UtWeb_Test extends App_Test {

    @Test
    public void testGMT() throws Exception {
        XDateTime d1 = UtDateTime.now().clearMSec();
        String s1 = UtWeb.dateToStringGMT(d1);
        XDateTime d2 = UtWeb.stringToDateGMT(s1);

        System.out.println(d1);
        System.out.println(d2);
        System.out.println(s1);

        assertEquals(d1, d2);
    }

    @Test
    public void test_isAbsoluteUrl() throws Exception {
        assertEquals(UtWeb.isAbsoluteUrl("http://asss/sss"), true);
        assertEquals(UtWeb.isAbsoluteUrl("/asss/sss"), false);
    }

}
