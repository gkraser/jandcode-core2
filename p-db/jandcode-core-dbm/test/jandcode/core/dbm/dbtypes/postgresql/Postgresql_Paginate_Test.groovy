package jandcode.core.dbm.dbtypes.postgresql

import jandcode.core.dbm.dbtypes.base.*
import jandcode.core.dbm.test.*
import jandcode.core.store.*
import org.junit.jupiter.api.*

class Postgresql_Paginate_Test extends Dbm_Test {

    @Test
    void create_t1() throws Exception {
        Store st = mdb.createStore(Table1)
        for (id in 1..20) {
            st.add(id: id, text: "text-${id}")
        }
        dbm.createDbTable("t1", st)
    }

    @Test
    public void paginate() throws Exception {
        create_t1()
        StoreRecord rec = mdb.loadQueryRecord("select count(*) as cnt from t1")
        println rec.getLong("cnt")

        //
        String sql = """
            select * from t1 order by id
            
            limit 3 offset 19 

        """
        def st = mdb.loadQuery(sql)
        utils.outTable(st)
    }

    @Test
    public void paginateParams() throws Exception {
        create_t1()
        StoreRecord rec = mdb.loadQueryRecord("select count(*) as cnt from t1")
        println rec.getLong("cnt")

        //
        String sql = """
            select * from t1 order by id
            
            limit :limit offset :offset

        """
        def st = mdb.loadQuery(sql, [limit: 3, offset: 18])
        utils.outTable(st)
    }



}
