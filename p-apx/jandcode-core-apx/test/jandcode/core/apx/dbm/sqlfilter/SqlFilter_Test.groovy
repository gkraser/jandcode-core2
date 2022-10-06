package jandcode.core.apx.dbm.sqlfilter


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
            ctx.addWhere("t1.f1=:${ctx.paramName()}")
            ctx.setParam(ctx.paramName(), ctx.getValue())
        })

        f.addWhere("p2", "equal")

        //
        out(f)
        //
        assertEquals(f.sql.toString(), "select * from t1 where t1.f1=:p1__value and p2=:p2__value and 0=0")
    }

    @Test
    public void attrs1() throws Exception {
        SqlFilter f = SqlFilter.create(mdb,
                "select * from t1 where 0=0",
                [p1: 1, p2: [value: 2], p3: [a: 1]]
        )

        //
        f.addWhere("p1", [a1: 1]) { ctx ->
            assertEquals(ctx.name, "p1")
            assertEquals(ctx.key, "p1")
            assertEquals(ctx.sqlField, "p1")
            assertEquals(ctx.attrs, [a1: 1])
        }

        //
        f.addWhere("p1", [a1: 1, sqlField: 'f1', key: 'k1']) { ctx ->
            assertEquals(ctx.name, "p1")
            assertEquals(ctx.key, "k1")
            assertEquals(ctx.sqlField, "f1")
            assertEquals(ctx.attrs, [a1: 1, sqlField: 'f1', key: 'k1'])
        }

    }


}
