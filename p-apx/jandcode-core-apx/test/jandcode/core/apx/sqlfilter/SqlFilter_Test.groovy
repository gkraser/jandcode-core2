package jandcode.core.apx.sqlfilter

import jandcode.core.apx.dbm.sqlfilter.*
import jandcode.core.dbm.test.*
import org.junit.jupiter.api.*

import static org.junit.jupiter.api.Assertions.*

class SqlFilter_Test extends Dbm_Test {

    void out(SqlFilter f) {
        utils.delim("orig sql")
        println f.getOrigSql()

        utils.delim("orig params")
        println f.getOrigParams()

        utils.delim("sql")
        println f.getSql()

        utils.delim("sqlCount")
        println f.getSqlCount()

        utils.delim("params")
        println f.getParams()
    }


    @Test
    public void test1() throws Exception {
        String sql = "select * from t1 where 0=0"
        Map params = [
                p1: 'v1',
                p2: [
                        value: 'v2'
                ],
                p3: [
                        value1: 'v3'
                ]
        ]
        SqlFilter f = SqlFilter.create(mdb, sql, params)

        f.addWhere("p1", { ctx ->
            ctx.addWhere("t1.f1=:${ctx.param()}")
            ctx.setParam(ctx.param(), ctx.getValue())
        })

        f.addWhere("p2", "equal")

        //
        out(f)
    }

    @Test
    public void attrs1() throws Exception {
        SqlFilter f = SqlFilter.create(mdb,
                "select * from t1 where 0=0",
                [p1: 1, p2: [value: 2], p3: [a: 1]]
        )

        SqlFilterWhere wh

        //
        wh = f.addWhere("p1", "equal", [a1: 1])
        assertEquals(wh.name, "p1")
        assertEquals(wh.key, "p1")
        assertEquals(wh.sqlField, "p1")
        assertEquals(wh.attrs, [a1: 1])

        //
        wh = f.addWhere("p1", "equal", [a1: 1, sqlField: 'f1', key: 'k1'])
        assertEquals(wh.name, "p1")
        assertEquals(wh.key, "k1")
        assertEquals(wh.sqlField, "f1")
        assertEquals(wh.attrs, [a1: 1, sqlField: 'f1', key: 'k1'])

    }


}
