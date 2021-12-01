package jandcode.core.dbm.dbtypes.base

import jandcode.core.dbm.test.*
import jandcode.core.store.*
import org.junit.jupiter.api.*

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

}
