package jandcode.db.oracle

import jandcode.core.test.*
import jandcode.db.*
import org.junit.jupiter.api.*

import static org.junit.jupiter.api.Assertions.*

//@Ignore
class OracleDbManager_Test extends App_Test {

    @Test
    public void exists1() throws Exception {
        DbService svc = app.bean(DbService)
        //
        DbSource dbs = svc.createDbSource("test.oracle.create")
        DbManagerService mng = dbs.bean(DbManagerService)
        //
        println mng.existDatabase()
    }

    @Test
    public void test1() throws Exception {
        DbService svc = app.bean(DbService)
        //
        DbSource dbs = svc.createDbSource("test.oracle.create")
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
