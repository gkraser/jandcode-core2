package jandcode.core.dbm.sql

import jandcode.commons.conf.*
import jandcode.core.dbm.test.*
import org.junit.jupiter.api.*

import static org.junit.jupiter.api.Assertions.*

class SqlText_Test extends Dbm_Test {

    SqlText create(String text) {
        return model.bean(SqlService).createSqlText(text)
    }

    SqlText create(Conf conf) {
        return model.bean(SqlService).createSqlText(conf)
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

    @Test
    public void replace_select1() throws Exception {
        SqlText sql = create("select * from tab1")

        sql.replaceSelect("id", true)
        assertEquals(sql.toString(), "select id, * from tab1")

        sql.replaceSelect("id", false)
        assertEquals(sql.toString(), "select id from tab1")
    }

    @Test
    public void replace_where1() throws Exception {
        SqlText sql = create("select * from tab1 where 0=0")

        sql.replaceWhere(["id=0", "id1=1"])
        assertEquals(sql.toString(), "select * from tab1 where id=0 and id1=1 and 0=0")
    }

    @Test
    public void replace_orderBy1() throws Exception {
        SqlText sql = create("select * from tab1 order by id")

        sql.replaceOrderBy("text")
        assertEquals(sql.toString(), "select * from tab1 order by text")

        sql.replaceOrderBy(null)
        assertEquals(sql.toString(), "select * from tab1 ") // пробел в конце!
    }

    //////

    @Test
    public void conf_text1() throws Exception {
        SqlText sql = create(model.conf.getConf("sql/s1.text"))
        assertEquals(sql.toString(), "s1!")
    }

    @Test
    public void conf_file1() throws Exception {
        SqlText sql = create(model.conf.getConf("sql/s1.file"))
        assertEquals(sql.toString(), "s1.sql!")
    }

    @Test
    public void conf_file_gsp1() throws Exception {
        SqlText sql = create(model.conf.getConf("sql/s1.file.gsp"))
        assertEquals(sql.toString(), "model=testdb conf=s1.file.gsp!")
    }


}
