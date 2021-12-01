package jandcode.core.dbm.model

import jandcode.core.db.*
import jandcode.core.dbm.impl.*
import jandcode.core.dbm.test.*
import org.junit.jupiter.api.*

import static org.junit.jupiter.api.Assertions.*

class ModelDbSource_Test extends Dbm_Test {

    @Test
    void simple_load_sql() throws Exception {
        def a = mdb.createStore()
        a.addField("id", "long")
        a.addField("name", "string", 20)
        a.add(id: 1, name: 'n1')
        //
        utils.outTable(a)
        //
        dbm.createDbTable("t1", a)
        //
        def st = mdb.loadQuery("select * from t1")
        utils.outTable(st)
    }

    @Test
    void db_wrapper_auto_connect() {
        Db db = dbm.model.createDb(true)
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

    @Test
    public void model_db_instance() throws Exception {
        Db db = dbm.model.createDb(true)
        assertEquals(db instanceof ModelDb, true)
    }

}
