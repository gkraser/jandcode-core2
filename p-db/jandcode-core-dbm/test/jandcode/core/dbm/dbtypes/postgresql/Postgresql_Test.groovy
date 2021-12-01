package jandcode.core.dbm.dbtypes.postgresql

import jandcode.commons.reflect.*
import jandcode.core.dbm.test.*
import jandcode.core.store.*
import org.junit.jupiter.api.*

public class Postgresql_Test extends Dbm_Test {

    class Table1 {
        long id

        @FieldProps(size = 20)
        String text
    }

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
    public void q1() throws Exception {
        Store st = mdb.createStore(Table1)
        st.add(id: 1, text: "text1")
        dbm.insertDbTable("t1", st)
        Store z = mdb.loadQuery("select * from t1")
        utils.outTable(z)
    }


}
