package jandcode.db.derby

import jandcode.core.test.*
import jandcode.db.*
import org.junit.jupiter.api.*

import static org.junit.jupiter.api.Assertions.*

class DerbyDbManager_Test extends App_Test {

    @Test
    public void test1() throws Exception {
        DbService svc = app.bean(DbService)
        //
        DbSource dbs = svc.createDbSource("test.derby.create")
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
