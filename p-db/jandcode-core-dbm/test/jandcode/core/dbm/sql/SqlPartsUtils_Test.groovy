package jandcode.core.dbm.sql

import jandcode.core.dbm.sql.impl.*
import jandcode.core.dbm.test.*
import org.junit.jupiter.api.*

import static org.junit.jupiter.api.Assertions.*;

class SqlPartsUtils_Test extends Dbm_Test {

    void check_where(String name, List wh, String sql, String sqlRes) {
        String res = SqlPartsUtils.replaceWhere(sql, name, wh)
        println res
        assertEquals(sqlRes, res)
    }

    void check_select(String text, boolean append, String sql, String sqlRes) {
        String res = SqlPartsUtils.replaceSelect(sql, text, append)
        println res
        assertEquals(sqlRes, res)
    }

    void check_orderby(String text, String sql, String sqlRes) {
        String res = SqlPartsUtils.replaceOrderBy(sql, text)
        println res
        assertEquals(sqlRes, res)
    }

    @Test
    public void test_where() throws Exception {
        check_where('default', ['z=0', 'x=1'],
                """select * \nfrom aaa where 0=0 order by id""",
                """select * \nfrom aaa where z=0 and x=1 and 0=0 order by id"""
        )
        check_where('default', ['z=0', 'x=1'],
                """select * from aaa where one /**/where 0=0 order by id""",
                """select * from aaa where one where z=0 and x=1 and 0=0 order by id"""
        )
        check_where('default', ['z=0', 'x=1'],
                """select * from aaa where one where 0=0 /*where*/ order by id""",
                """select * from aaa where one where 0=0 and z=0 and x=1 order by id"""
        )
        check_where('default', ['z=0', 'x=1'],
                """select * from aaa where one where 0=0 /*where:default*/ order by id""",
                """select * from aaa where one where 0=0 and z=0 and x=1 order by id"""
        )
        check_where('d', ['z=0', 'x=1'],
                """select * from aaa where one where 0=0 /*where:d*/ order by id""",
                """select * from aaa where one where 0=0 and z=0 and x=1 order by id"""
        )
    }


    @Test
    public void test_select() throws Exception {
        check_select('rownum', true,
                """select * from aaa""",
                """select rownum, * from aaa"""
        )
        check_select('rownum', true,
                """/**/select * from aaa select b""",
                """select rownum, * from aaa select b"""
        )
        check_select('rownum', true,
                """/**/select * /**/from aaa select b from""",
                """select rownum, * /**/from aaa select b from"""
        )
        check_select('rownum', false,
                """select * from aaa""",
                """select rownum from aaa"""
        )
        check_select('rownum', false,
                """/**/select * from aaa select b""",
                """select rownum from aaa select b"""
        )
        check_select('rownum', false,
                """/**/select * /**/from aaa select b from""",
                """select rownum from aaa select b from"""
        )
    }

    @Test
    public void test_orderby() throws Exception {
        check_orderby('a,b',
                """select * from aaa""",
                """select * from aaa""",
        )
        check_orderby('a,b',
                """select * from aaa order by b,n,m""",
                """select * from aaa order by a,b""",
        )
        check_orderby('a,b',
                """select * from aaa /**/ order by b,n,m""",
                """select * from aaa order by a,b""",
        )
        check_orderby('a,b',
                """select * from aaa /**/ order by b,n,m/*end*/""",
                """select * from aaa order by a,b""",
        )
        check_orderby('a,b',
                """select * from aaa /**/order by b,n,m /*end*/ order by b""",
                """select * from aaa order by a,b order by b""",
        )
    }
}
