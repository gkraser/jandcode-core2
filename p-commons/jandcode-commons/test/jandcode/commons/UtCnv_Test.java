package jandcode.commons;

import jandcode.commons.named.Named;
import jandcode.commons.test.*;
import org.junit.jupiter.api.*;

import java.awt.*;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class UtCnv_Test extends Utils_Test {

    @Test
    public void test_toDimension() throws Exception {
        assertEquals(UtCnv.toDimension(""), new Dimension(0, 0));
        assertEquals(UtCnv.toDimension("100,200"), new Dimension(100, 200));
        assertEquals(UtCnv.toDimension(",200"), new Dimension(0, 200));
        assertEquals(UtCnv.toDimension("100,"), new Dimension(100, 0));
        assertEquals(UtCnv.toDimension("  100, 200 "), new Dimension(100, 200));
    }

    @Test
    public void test_toInsets() throws Exception {
        assertEquals(UtCnv.toInsets(""), new Insets(0, 0, 0, 0));
        assertEquals(UtCnv.toInsets("1,2,3,4"), new Insets(1, 2, 3, 4));
        assertEquals(UtCnv.toInsets(",,,"), new Insets(0, 0, 0, 0));
        assertEquals(UtCnv.toInsets(",4,,5"), new Insets(0, 4, 0, 5));
        assertEquals(UtCnv.toInsets("1,2"), new Insets(1, 2, 1, 2));
        assertEquals(UtCnv.toInsets("1"), new Insets(1, 0, 1, 0));
        assertEquals(UtCnv.toInsets(",1"), new Insets(0, 1, 0, 1));
    }

    @Test
    public void test_toMap() throws Exception {
        assertEquals(UtCnv.toMap("").toString(), "{}");
        assertEquals(UtCnv.toMap("a:b;c:d").toString(), "{a=b, c=d}");
        assertEquals(UtCnv.toMap("a;c:d;").toString(), "{a=, c=d}");
    }

    @Test
    @SuppressWarnings("unchecked")
    public void test_toMap_arr() throws Exception {
        assertEquals(UtCnv.toMap().toString(), "{}");
        assertEquals(UtCnv.toMap("a", "b", "c", "d").toString(), "{a=b, c=d}");
        //
        Map m1 = new HashMap();
        m1.put("a", "1");
        Map m2 = new HashMap();
        m2.put("b", "2");
        assertEquals(UtCnv.toMap(m1, "z", "3", m2).toString(), "{a=1, z=3, b=2}");
    }

    @Test
    public void test_toList() throws Exception {
        assertEquals(UtCnv.toList("").toString(), "[]");
        assertEquals(UtCnv.toList("1,2,3").toString(), "[1, 2, 3]");
        assertEquals(UtCnv.toList(" ,,,  ,2 , 3 ,,,  4  5 ").toString(), "[2, 3, 4, 5]");
    }

    @Test
    public void test_toDateTime() throws Exception {
        assertEquals(UtCnv.toDateTime("").toString(), "0000-01-01");
        assertEquals(UtCnv.toDateTime("1999").toString(), "0000-01-01");
        assertEquals(UtCnv.toDateTime("1999-12").toString(), "0000-01-01");
        assertEquals(UtCnv.toDateTime("1999-12-23").toString(), "1999-12-23");
        long a = 1382432226328L;
        assertEquals(UtCnv.toDateTime(a).toString(), "2013-10-22T14:57:06.328");
        assertEquals(UtCnv.toDateTime(new Date(a)).toString(), "2013-10-22T14:57:06.328");
        assertEquals(UtCnv.toDateTime(new java.sql.Date(a)).toString(), "2013-10-22T14:57:06.328");
        assertEquals(UtCnv.toDateTime(new java.sql.Time(a)).toString(), "2013-10-22T14:57:06.328");
        assertEquals(UtCnv.toDateTime(new java.sql.Timestamp(a)).toString(), "2013-10-22T14:57:06.328");
    }

    @Test
    public void test_toDate() throws Exception {
        assertEquals(UtCnv.toDate("").toString(), "0000-01-01");
        assertEquals(UtCnv.toDate("1999").toString(), "0000-01-01");
        assertEquals(UtCnv.toDate("1999-12").toString(), "0000-01-01");
        assertEquals(UtCnv.toDate("1999-12-23").toString(), "1999-12-23");
        long a = 1382432226328L;
        assertEquals(UtCnv.toDate(a).toString(), "2013-10-22");
        assertEquals(UtCnv.toDate(new Date(a)).toString(), "2013-10-22");
        assertEquals(UtCnv.toDate(new java.sql.Date(a)).toString(), "2013-10-22");
        assertEquals(UtCnv.toDate(new java.sql.Time(a)).toString(), "2013-10-22");
        assertEquals(UtCnv.toDate(new java.sql.Timestamp(a)).toString(), "2013-10-22");
    }

    @Test
    public void test_toName() throws Exception {
        assertEquals(UtCnv.toName(null), "");
        assertEquals(UtCnv.toName("a"), "a");

        Named b = new Named();
        b.setName("b");
        assertEquals(UtCnv.toName(b), "b");
    }

    @Test
    public void test_toNameList() throws Exception {
        assertEquals(UtCnv.toNameList(null).toString(), "[]");

        Named a = new Named();
        a.setName("a");
        assertEquals(UtCnv.toNameList(Arrays.asList(1, a, "b", null)).toString(), "[1, a, b]");

        //
        assertEquals(UtCnv.toNameList("a").toString(), "[a]");
        assertEquals(UtCnv.toNameList("a;b").toString(), "[a, b]");
    }

    @Test
    public void test_toRadix() throws Exception {
        assertEquals(UtCnv.toRadix(255, 16, -1), "ff");
        assertEquals(UtCnv.toRadix(255, 64, -1), "3-");
        assertEquals(UtCnv.toRadix(255, 64, 4), "003-");
    }

    @Test
    public void test_toLong() throws Exception {
        assertEquals(UtCnv.toLong(255.55), 255);
        assertEquals(UtCnv.toLong("255.55"), 255);
        assertEquals(UtCnv.toLong("255"), 255);
    }

    @Test
    public void test_toInt() throws Exception {
        assertEquals(UtCnv.toInt(255.55), 255);
        assertEquals(UtCnv.toInt("255.55"), 255);
        assertEquals(UtCnv.toInt("255"), 255);
    }

    @Test
    public void test_round() throws Exception {
        assertEquals(UtCnv.round(156.123, 2), 156.12);
        assertEquals(UtCnv.round(156.123, -2), 200);
        assertEquals(UtCnv.round(1.0 / 6.0, 3), 0.167);
        assertEquals(UtCnv.round(1.0 / 3.0, 1), 0.3);
    }

}
