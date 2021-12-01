package jandcode.core.dbm.dbtypes.base

import jandcode.core.dbm.sql.*
import jandcode.core.dbm.test.*
import jandcode.core.store.*
import org.junit.jupiter.api.*

import static org.junit.jupiter.api.Assertions.*;

abstract class BasePaginate_Test extends Dbm_Test {

    SqlUtils sqlUtils

    void setUp() throws Exception {
        super.setUp()
        //
        sqlUtils = SqlUtils.create(getModel())
    }

    protected void create_t1() throws Exception {
        Store st = mdb.createStore(Table1)
        for (id in 1..20) {
            st.add(id: id, text: "text-${id}")
        }
        dbm.createDbTable("t1", st)
    }

    void check(int offset, int limit, int resCount, long resIdFirst = -1, long resIdLast = -1) {
        //
        String sql = """
            select * from t1 order by id
        """

        // by param
        String sqlPg = sqlUtils.paginate(sql)

        Store st = mdb.loadQuery(sqlPg, [offset: offset, limit: limit])

        assertEquals(st.countFields, 2, "count fields")

        assertEquals(st.size(), resCount, "count records")
        if (resCount > 0) {
            assertEquals(st.get(0).getLong("id"), resIdFirst, "first id")
            assertEquals(st.get(st.size() - 1).getLong("id"), resIdLast, "first id")
        }

        // by text
        sqlPg = sqlUtils.paginate(sql, offset, limit)

        st = mdb.loadQuery(sqlPg)

        assertEquals(st.countFields, 2, "count fields")

        assertEquals(st.size(), resCount, "count records")
        if (resCount > 0) {
            assertEquals(st.get(0).getLong("id"), resIdFirst, "first id")
            assertEquals(st.get(st.size() - 1).getLong("id"), resIdLast, "first id")
        }

    }

    @Test
    public void paginate() throws Exception {
        create_t1()

        check(0, 3, 3, 1, 3)
        check(1, 3, 3, 2, 4)
        check(18, 3, 2, 19, 20)
        check(19, 3, 1, 20, 20)
        check(20, 3, 0, -1, -1)
        check(200, 3, 0, -1, -1)
    }


}
