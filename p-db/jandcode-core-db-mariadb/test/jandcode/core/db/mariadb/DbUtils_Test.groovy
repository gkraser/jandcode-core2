package jandcode.core.db.mariadb

import jandcode.core.db.test.*
import jandcode.core.test.*
import org.junit.jupiter.api.*

class DbUtils_Test extends App_Test {

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


}
