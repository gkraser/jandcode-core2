package jandcode.core.db.postgresql

import jandcode.core.db.*
import jandcode.core.test.*
import org.junit.jupiter.api.*

import static org.junit.jupiter.api.Assertions.*

class PostgresqlDbManager_Test extends App_Test {

    @Test
    public void systemDb() throws Exception {
        DbService svc = app.bean(DbService)
        //
        DbSource dbs = svc.createDbSource("test.create")
        jandcode.core.db.std.BaseDbManagerService dm = dbs.bean(DbManagerService)
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
