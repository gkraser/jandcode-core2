package jandcode.db.postgresql

import jandcode.core.test.*
import jandcode.db.*
import org.junit.jupiter.api.*

import static org.junit.jupiter.api.Assertions.*

//@Ignore
class PostgresqlDbManager_Test extends App_Test {

    @Test
    public void overrideSystemProps() throws Exception {
        DbService svc = app.bean(DbService)
        //
        DbSource dbs = svc.createDbSource("test.postgresql.create")
        utils.outMap(dbs.props)
        //
        utils.delim("")
        utils.outMap(dbs.getProps("system", true))
    }

    @Test
    public void exists1() throws Exception {
        DbService svc = app.bean(DbService)
        //
        DbSource dbs = svc.createDbSource("test.postgresql.create")
        DbManagerService mng = dbs.bean(DbManagerService)
        //
        println mng.existDatabase()
    }

    @Test
    public void test1() throws Exception {
        DbService svc = app.bean(DbService)
        //
        DbSource dbs = svc.createDbSource("test.postgresql.create")
        DbManagerService mng = dbs.bean(DbManagerService)
        //

        if (mng.existDatabase()) {
            mng.dropDatabase()
        }
        mng.createDatabase()
        assertEquals(mng.existDatabase(), true)
        mng.dropDatabase()
        assertEquals(mng.existDatabase(), false)
        //
    }

}
