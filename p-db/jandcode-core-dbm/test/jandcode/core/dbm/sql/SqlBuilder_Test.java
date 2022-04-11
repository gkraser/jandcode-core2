package jandcode.core.dbm.sql;

import jandcode.commons.*;
import jandcode.core.dbm.test.*;
import org.junit.jupiter.api.*;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class SqlBuilder_Test extends Dbm_Test {

    SqlBuilder sb;

    public void setUp() throws Exception {
        super.setUp();
        //
        sb = getModel().bean(SqlService.class).createSqlBuilder();
    }

    @Test
    public void test_makeFieldList() throws Exception {
        //
        List<String> z;

        z = sb.makeFieldList("a,b,,c");
        assertEquals(z.toString(), "[a, b, c]");

        z = sb.makeFieldList(getMdb().getDomain("tab1"));
        assertEquals(z.toString(), "[id, a, b, c]");

        z = sb.makeFieldList(UtCnv.toMap("a", 1, "b", 1, "c", 1));
        assertEquals(z.toString(), "[a, b, c]");

        z = sb.makeFieldList(null);
        assertEquals(z.toString(), "[]");

        z = sb.makeFieldList("a,b,,c,C,c,b,B,A");
        assertEquals(z.toString(), "[a, b, c]");
    }

    @Test
    public void test_makeFieldList_exclude() throws Exception {
        List<String> z;

        z = sb.makeFieldList("a,b,,c", "c");
        assertEquals(z.toString(), "[a, b]");

        z = sb.makeFieldList(getMdb().getDomain("tab1"), "A,C");
        assertEquals(z.toString(), "[id, b]");
    }

    @Test
    public void test_makeSqlInsert() throws Exception {
        //
        String z;

        z = sb.makeSqlInsert("t1", "a,b,,c");
        assertEquals(z, "insert into t1(a,b,c) values (:a,:b,:c)");
    }

    @Test
    public void test_makeSqlUpdate() throws Exception {
        //
        String z;

        z = sb.makeSqlUpdate("t1", "a,b,,c", "d");
        assertEquals(z, "update t1 set a=:a,b=:b,c=:c where d=:d");

        z = sb.makeSqlUpdate("t1", "a,b,,c", "d,e");
        assertEquals(z, "update t1 set a=:a,b=:b,c=:c where d=:d and e=:e");

    }

    @Test
    public void test_makeSqlDelete() throws Exception {
        //
        String z;

        z = sb.makeSqlDelete("t1", "d");
        assertEquals(z, "delete from t1 where d=:d");

        z = sb.makeSqlDelete("t1", "d,e");
        assertEquals(z, "delete from t1 where d=:d and e=:e");
    }

}