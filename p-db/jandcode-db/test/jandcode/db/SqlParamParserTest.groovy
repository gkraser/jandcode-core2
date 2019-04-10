package jandcode.db

import jandcode.core.test.*
import jandcode.db.impl.*
import org.junit.jupiter.api.*

import static org.junit.jupiter.api.Assertions.*

class SqlParamParserTest extends App_Test {

    void check(String sql, String res, String params) {
        def p = new SqlParamParser(sql)
        assertEquals(p.result, res)
        assertEquals(p.params.toString(), params)
    }

    @Test
    public void test1() throws Exception {
        check("", "", "[]")
        check("asd", "asd", "[]")
        check("aa :b c :{dd} ee", "aa ? c ? ee", "[b, dd]")
        check(":b c :{dd}", "? c ?", "[b, dd]")
        check(":b c :{dd}:z", "? c ??", "[b, dd, z]")
    }


}
