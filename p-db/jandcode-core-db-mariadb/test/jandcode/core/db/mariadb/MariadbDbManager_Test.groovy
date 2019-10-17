package jandcode.core.db.mariadb

import jandcode.core.db.*
import jandcode.core.test.*
import org.junit.jupiter.api.*

import static org.junit.jupiter.api.Assertions.*

class MariadbDbManager_Test extends App_Test {

    @Test
    public void exists1() throws Exception {
        DbService svc = app.bean(DbService)
        //
        DbSource dbs = svc.createDbSource("test.create")
        utils.outMap(dbs.props)
        utils.outMap(dbs.conf)
        DbManagerService mng = dbs.bean(DbManagerService)
        //
        println mng.existDatabase()
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
