package jandcode.db.oracle

import jandcode.core.test.*
import jandcode.db.*
import jandcode.db.std.BaseDbManagerService
import org.junit.jupiter.api.*

import static org.junit.jupiter.api.Assertions.*

class OracleDbManager_Test extends App_Test {

    @Test
    public void systemDb() throws Exception {
        DbService svc = app.bean(DbService)
        //
        DbSource dbs = svc.createDbSource("test.create")
        BaseDbManagerService dm = dbs.bean(DbManagerService)
        DbSource sysDbs = dm.getSystemDbSource()
        utils.outMap(sysDbs.getProps())
    }


    @Test
    public void test1() throws Exception {
        DbService svc = app.bean(DbService)
        //
        DbSource dbs = svc.createDbSource("test.create")
        DbManagerService mng = dbs.bean(DbManagerService)
        //

        Db db = dbs.createDb(true)

        if (mng.existDatabase()) {
            mng.dropDatabase()
        }
        assertEquals(mng.existDatabase(), false)

        mng.createDatabase()
        assertEquals(mng.existDatabase(), true)
        db.connect()
        db.disconnect()

        mng.dropDatabase()
        assertEquals(mng.existDatabase(), false)
        //
    }

}
