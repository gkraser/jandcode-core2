package jandcode.core.dbm.sql

import jandcode.core.dbm.test.*
import org.junit.jupiter.api.*

import static org.junit.jupiter.api.Assertions.*;

class SqlText_Test extends Dbm_Test {

    SqlText create(String text) {
        SqlText sql = model.create(SqlText)
        sql.setSql(text)
        return sql
    }

    @Test
    public void sql_text() throws Exception {
        String s = "select * from tab1"
        SqlText sql = create(s)
        assertEquals(sql.getOrigSql(), s)
        assertEquals(sql.getSql(), s)
        assertEquals(sql.toString(), s)
    }

    @Test
    public void paginate1() throws Exception {
        SqlText sql = create("select * from tab1")

        sql.paginate(true)
        assertEquals(sql.toString(), "select * from tab1 limit :limit offset :offset")

        sql.paginateParamsPrefix("a_")
        assertEquals(sql.toString(), "select * from tab1 limit :a_limit offset :a_offset")
    }


}
