package jandcode.core.db.mariadb

import jandcode.core.db.test.*
import jandcode.core.test.*
import org.junit.jupiter.api.*

class Mariadb_DbUtils_Test extends App_Test {

    DbSimpleTestSvc z = testSvc(DbSimpleTestSvc)

    @Test
    public void test1() throws Exception {
        def a = z.createStore()
        a.addField("id", "long")
        a.addField("name", "string", 20)
        a.add(id: 1, name: 'n1')
        //
        utils.outTable(a)
        //
        z.createTable("t1", a)
        //
    }

    @Test
    public void charcase_loaded_store_fields() throws Exception {
        def a = z.createStore()
        a.addField("ID", "int")
        a.addField("field_lower", "int")
        a.addField("FIELD_UPPER", "int")
        a.addField("fieldMix", "int")
        a.addField("FieldMixU", "int")

        a.add(id: 1)

        z.createTable("t1", a)
        //
        def store = z.db.loadQuery("select * from t1")
        utils.outTable(store)
    }


}
