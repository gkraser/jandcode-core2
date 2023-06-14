package jandcode.commons.sql;


import jandcode.commons.test.*;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

class SqlParamParser_Test extends Utils_Test {

    void check(String sql, String res, String params) {
        SqlParamParser p = new SqlParamParser(sql);
        assertEquals(p.getResult(), res);
        assertEquals(p.getParams().toString(), params);
    }

    @Test
    public void test1() throws Exception {
        check("", "", "[]");
        check("asd", "asd", "[]");
        check("aa :b c :{dd} ee", "aa ? c ? ee", "[b, dd]");
        check(":b c :{dd}", "? c ?", "[b, dd]");
        check(":b c :{dd}:z", "? c ??", "[b, dd, z]");
        check(":b::c", "?::c", "[b]");
    }

    @Test
    public void test_param_is_idn() throws Exception {
        check("a : b :йц", "a : b :йц", "[]");
        check("a :{} :{привет} :{q} :{q1} b", "a :{} :{привет} ? ? b", "[q, q1]");
    }

    @Test
    public void test_comment_line() throws Exception {
        check("a :b --:z", "a ? --:z", "[b]");
        check("a :b --:z\n", "a ? --:z\n", "[b]");
        check("--a :b --:z", "--a :b --:z", "[]");
    }

    @Test
    public void test_comment_multi() throws Exception {
        check("a /*:b --:z*/", "a /*:b --:z*/", "[]");
        check("a /*:b \n--:z*/", "a /*:b \n--:z*/", "[]");
        check("a /*:b --:z", "a /*:b --:z", "[]");
        check("a /*:b --:z*", "a /*:b --:z*", "[]");
    }

}
