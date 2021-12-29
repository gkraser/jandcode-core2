package jandcode.core.dbm.dbtypes.base

import jandcode.commons.*
import jandcode.commons.datetime.*
import jandcode.core.dbm.test.*
import jandcode.core.store.*
import org.junit.jupiter.api.*

import static org.junit.jupiter.api.Assertions.*;

/**
 * Базовый тест для проверки работоспособности некоторых вещей в db
 */
abstract class BaseCheckDb_Test extends Dbm_Test {

    @Test
    public void info() throws Exception {
        utils.outMap(dbm.getDb().getDbSource().getProps())
    }

    @Test
    public void createTestDb() throws Exception {
        Store st = mdb.createStore(Table1)
        st.add(id: 1, text: "text1")
        dbm.createDbTable("t1", st)
        Store z = mdb.loadQuery("select * from t1")
        utils.outTable(z)
    }

    @Test
    public void sql_values() throws Exception {
        //utils.logOn()
        //
        Store st = mdb.createStore()
        st.addField("id", "long")
        st.addField("f_long", "long")
        st.addField("f_int", "int")
        st.addField("f_double", "double")
        st.addField("f_boolean", "boolean")
        st.addField("f_string", "string", 30)
        st.addField("f_date", "date")
        st.addField("f_datetime", "datetime")
        //
        st.add(
                id: 1,
                f_long: 12345,
                f_int: 123,
                f_double: 123.56,
                f_boolean: true,
                f_string: "strin'g-с'трока",
                f_date: XDate.create("2020-11-22"),
                f_datetime: XDateTime.create("2020-11-22T12:34:56"),
        )
        //
        dbm.createDbTable("t1", st)

        def dmn = mdb.createDomain(st)
        def rec = st.get(0)

        List<String> flds = []
        List<String> vls = []
        for (f in dmn.fields) {
            flds.add(f.name)
            vls.add(f.getSqlValue(rec.getValue(f.name)))
        }

        String sql = "insert into t1 (${UtString.join(flds, ',')})\nvalues (${UtString.join(vls, ',')})"
        println sql
        mdb.execQueryNative(sql)

        //
        Store st2 = mdb.loadQuery('select * from t1 order by id')
        utils.outTable(st2)
        assertEquals(st2.get(0).values, st2.get(1).values)
    }


}
