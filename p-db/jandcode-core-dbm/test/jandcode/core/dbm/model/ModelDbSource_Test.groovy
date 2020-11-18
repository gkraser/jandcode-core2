package jandcode.core.dbm.model

import jandcode.core.db.*
import jandcode.core.dbm.*
import jandcode.core.dbm.impl.*
import jandcode.core.test.*
import org.junit.jupiter.api.*

import static org.junit.jupiter.api.Assertions.*;

class ModelDbSource_Test extends App_Test {

    DbmDbTestSvc z = testSvc(DbmDbTestSvc)

    @Test
    void simple_load_sql() throws Exception {
        def a = z.createStore()
        a.addField("id", "long")
        a.addField("name", "string", 20)
        a.add(id: 1, name: 'n1')
        //
        utils.outTable(a)
        //
        z.createTable("t1", a)
        //
        def st = z.db.loadQuery("select * from t1")
        utils.outTable(st)
    }

    @Test
    void db_wrapper_auto_connect() {
        Db db = z.model.createDb(true)
        ModelDbWrapper dbw = new ModelDbWrapper(db, true, true)

        //
        assertEquals(db.isConnected(), false)
        assertEquals(db.isTran(), false)
        assertEquals(dbw.isConnected(), false)
        assertEquals(dbw.isTran(), false)

        //
        try {
            // not connected
            db.loadQuery("select 1")
            fail()
        } catch (ignore) {
        }

        // connected
        dbw.loadQuery("select 1")

        //
        assertEquals(db.isConnected(), true)
        assertEquals(db.isTran(), true)
        assertEquals(dbw.isConnected(), true)
        assertEquals(dbw.isTran(), true)

        //
        dbw.rollback()
        dbw.disconnect()

        //
        assertEquals(db.isConnected(), false)
        assertEquals(db.isTran(), false)
        assertEquals(dbw.isConnected(), false)
        assertEquals(dbw.isTran(), false)

    }

}
