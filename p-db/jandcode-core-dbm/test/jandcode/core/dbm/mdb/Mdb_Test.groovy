package jandcode.core.dbm.mdb

import jandcode.core.dbm.*
import jandcode.core.dbm.dao.data.*
import jandcode.core.test.*
import org.junit.jupiter.api.*

import static org.junit.jupiter.api.Assertions.*

class Mdb_Test extends App_Test {

    DbmDbTestSvc z = testSvc(DbmDbTestSvc)

    Mdb getMdb() {
        return z.mdb
    }

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
        def st = mdb.loadQuery("select * from t1")
        utils.outTable(st)
    }

    @Test
    public void dao1() throws Exception {
        CheckModelLink dao = mdb.createDao(CheckModelLink)
        assertEquals(dao.m1(), "m1-ok-test1");
    }


}
