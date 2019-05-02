package jandcode.db.h2

import jandcode.core.test.*
import jandcode.db.*
import org.junit.jupiter.api.*

import static org.junit.jupiter.api.Assertions.*

class H2MemDbManager_Test extends App_Test {

    @Test
    public void test1() throws Exception {
        DbService svc = app.bean(DbService)
        //
        DbSource dbs = svc.createDbSource("test1")
        DbManagerService mng = dbs.bean(DbManagerService)
        //

        Db db = dbs.createDb(true)

        assertEquals(mng.existDatabase(), true)
        db.connect()
        db.disconnect()

        mng.dropDatabase()
        assertEquals(mng.existDatabase(), true)
    }

}
