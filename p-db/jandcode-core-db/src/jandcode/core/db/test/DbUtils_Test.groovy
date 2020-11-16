package jandcode.core.db.test

import jandcode.commons.*
import jandcode.core.test.*
import org.junit.jupiter.api.*

import static org.junit.jupiter.api.Assertions.*

/**
 * Базовый класс для тестирования Db и DbUtils.
 * Используется как предок при тестировании каждого драйвера для унификации поведения.
 */
abstract class DbUtils_Test extends App_Test {

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
        def store = z.db.loadQuery('select t1.*, field_upper as "MyField" from t1')
        utils.outTable(store)
        //
        def flds = UtCnv.toNameList(store.fields).join(',')
        assertEquals(flds, "id,field_lower,field_upper,fieldmix,fieldmixu,myfield")
    }


}
