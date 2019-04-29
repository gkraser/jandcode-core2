package jandcode.db.mariadb

import jandcode.core.test.*
import jandcode.db.*
import org.junit.jupiter.api.*

import static org.junit.jupiter.api.Assertions.*

//@Ignore
class MariadbDbManager_Test extends App_Test {

    @Test
    public void exists1() throws Exception {
        DbService svc = app.bean(DbService)
        //
        DbSource dbs = svc.createDbSource("test.mariadb.create")
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
        DbSource dbs = svc.createDbSource("test.mariadb.create")
        DbManagerService mng = dbs.bean(DbManagerService)
        //

        if (mng.existDatabase()) {
            mng.dropDatabase()
        }
        assertEquals(mng.existDatabase(), false)

        mng.createDatabase()
        assertEquals(mng.existDatabase(), true)

        mng.dropDatabase()
        assertEquals(mng.existDatabase(), false)
        //
    }

}
