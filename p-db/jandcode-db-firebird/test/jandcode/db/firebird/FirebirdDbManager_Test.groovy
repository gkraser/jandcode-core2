package jandcode.db.firebird

import jandcode.core.test.*
import jandcode.db.*
import org.junit.jupiter.api.*

import static org.junit.jupiter.api.Assertions.*

class FirebirdDbManager_Test extends App_Test {

    @Test
    public void test1() throws Exception {
        DbService svc = app.bean(DbService)
        //
        DbSource dbs = svc.createDbSource("test.firebird.create")
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

    @Test
    public void exists1() throws Exception {
        DbService svc = app.bean(DbService)
        //
        DbSource dbs = svc.createDbSource("test.firebird.create")
        DbManagerService mng = dbs.bean(DbManagerService)
        mng.createDatabase()

        utils.outMap(dbs.props)

        //
        println mng.existDatabase()
    }


}
