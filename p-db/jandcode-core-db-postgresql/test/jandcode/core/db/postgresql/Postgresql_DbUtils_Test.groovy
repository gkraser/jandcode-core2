package jandcode.core.db.postgresql

import jandcode.core.db.test.*
import jandcode.core.test.*
import org.junit.jupiter.api.*

class Postgresql_DbUtils_Test extends App_Test {

    DbSimpleTestSvc z = testSvc(DbSimpleTestSvc)

    @Test
    void charcase_loaded_store_fields() throws Exception {
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
