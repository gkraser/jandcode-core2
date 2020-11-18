package jandcode.core.dbm.mdb

import jandcode.core.dbm.*
import jandcode.core.test.*
import org.junit.jupiter.api.*

class Mdb_Test extends App_Test {

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
        def st = z.mdb.loadQuery("select * from t1")
        utils.outTable(st)
    }

}
