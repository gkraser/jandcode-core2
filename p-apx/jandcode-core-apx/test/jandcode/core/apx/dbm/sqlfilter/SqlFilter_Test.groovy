package jandcode.core.apx.dbm.sqlfilter

import jandcode.core.apx.store.*
import jandcode.core.dbm.test.*
import jandcode.core.store.*
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

    @Test
    public void paginate1() throws Exception {
        String sql = "select * from t1 where 0=0"
        Map params = [
                paginate: [
                        limit : 5,
                        offset: 10,
                ],
        ]
        SqlFilter f = SqlFilter.create(mdb, sql, params)

        //
        out(f)
        //
        assertEquals(f.sql.toString(), "select * from t1 where 0=0 limit :paginate__limit offset :paginate__offset")
        assertEquals(f.params, [paginate: null, paginate__offset: 10, paginate__limit: 5])
    }

    @Test
    public void orderBy1() throws Exception {
        String sql = "select * from t1 where 0=0 order by id"
        Map params = [
                orderBy: "z1"
        ]
        SqlFilter f = SqlFilter.create(mdb, sql, params)

        f.addOrderBy("z1", "f1,f2")
        //
        out(f)
        //
        assertEquals(f.sql.toString(), "select * from t1 where 0=0 order by f1,f2")
    }

    @Test
    public void orderBy2() throws Exception {
        String sql = "select * from t1 where 0=0 order by id"
        Map params = [
                orderBy: "z2,z1"
        ]
        SqlFilter f = SqlFilter.create(mdb, sql, params)

        f.addOrderBy("z1", "f1,f2")
        f.addOrderBy("z2", "f3")
        //
        out(f)
        //
        assertEquals(f.sql.toString(), "select * from t1 where 0=0 order by f3,f1,f2")
    }

    @Test
    public void orderBy3_error() throws Exception {
        String sql = "select * from t1 where 0=0 order by id"
        Map params = [
                orderBy: "z2,z1_",
        ]
        SqlFilter f = SqlFilter.create(mdb, sql, params)

        f.addOrderBy("z1", "f1,f2")
        f.addOrderBy("z2", "f3")
        //
        try {
            out(f)
            fail()
        } catch (e) {
            utils.showError(e)
        }
    }

    void createData() {
        Store st = mdb.createStore()
        st.addField("id", "long")
        st.addField("f1", "long")
        //
        for (i in 1..10) {
            st.add(id: i, f1: i + 100)
        }
        //
        dbm.createDbTable("t1", st)
    }

    @Test
    public void load1_withPaginate() throws Exception {
        createData()
        //
        utils.logOn()
        String sql = "select * from t1 where 0=0"
        Map params = [
                paginate: [
                        limit : 2,
                        offset: 3,
                ],
        ]
        SqlFilter f = SqlFilter.create(mdb, sql, params)
        def st = f.load()
        utils.outTable(st)
        def pg = ApxStoreUtils.getPaginate(st)
        println pg
    }

    @Test
    public void emptyParams1() throws Exception {
        String sql = "select * from t1 where 0=0"
        Map params = [
                p0: 0,
                p1: null,
                p2: "",
                p3: [],
                p4: [:],
        ]
        SqlFilter f = SqlFilter.create(mdb, sql, params)
        //
        f.addWhere("p0", "equal")
        f.addWhere("p1", "equal")
        f.addWhere("p2", "equal")
        f.addWhere("p3", "equal")
        f.addWhere("p4", "equal")
        //
        out(f)
        //
        assertEquals(f.sql.toString(), "select * from t1 where p0=:p0__value and 0=0")
    }

    //////

    @Test
    public void part1() throws Exception {
        String sql = "select * from t1 /*part:p1*/ where 0=0 /*part:p2*/"
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
            ctx.addPart("p1", "left join f1=:${ctx.paramName()}")
            ctx.setParam(ctx.paramName(), ctx.getValue())
        })
        f.addWhere("p2", { ctx ->
            ctx.addPart("p1", "left join f2=:${ctx.paramName()}")
            ctx.setParam(ctx.paramName(), ctx.getValue())
        })

        //
        out(f)
        //
        assertEquals(f.sql.toString(), "select * from t1 left join f1=:p1__value left join f2=:p2__value where 0=0 /*part:p2*/")
    }

    @Test
    public void chain_builder() throws Exception {
        String sql = "select * from t1 /*part:p1*/ where 0=0 /*part:p2*/"
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
            ctx.addPart("p1", "z1")
        }).addBuilder({ctx->
            ctx.addPart("p1", "z2")
        })
        //
        out(f)
        //
        assertEquals(f.sql.toString(), "select * from t1 z1 z2 where 0=0 /*part:p2*/")
    }

}
