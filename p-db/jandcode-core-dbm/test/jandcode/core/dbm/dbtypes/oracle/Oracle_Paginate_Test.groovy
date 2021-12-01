package jandcode.core.dbm.dbtypes.oracle

import jandcode.core.dbm.dbtypes.base.*
import jandcode.core.dbm.test.*
import jandcode.core.store.*
import org.junit.jupiter.api.*

class Oracle_Paginate_Test extends Dbm_Test {

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
select * from ( select rownum as r__n, r__all.* from (

            select * from t1 order by id
            
) r__all ) where r__n>18 and r__n<=18+3
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
select * from ( select r__all.*, rownum as r__n from (

            select * from t1 order by id
            
) r__all ) where r__n>:offset and r__n<=:offset+:limit
        """
        def st = mdb.loadQuery(sql, [limit: 3, offset: 1])
        utils.outTable(st)
    }

    @Test
    public void paginateParams2() throws Exception {
        create_t1()
        StoreRecord rec = mdb.loadQueryRecord("select count(*) as cnt from t1")
        println rec.getLong("cnt")

        //
        String sql = """ 
select * from ( select r__all.*, rownum as r__n from (

            select * from t1 order by id
            
) r__all ) where r__n>:offset and r__n<=:offset+:limit
        """
        def st = mdb.loadQuery(sql, [limit: 3, offset: 1])
        utils.outTable(st)
    }


}
