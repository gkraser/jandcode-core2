package jandcode.core.db.mariadb

import jandcode.core.db.test.*
import org.junit.jupiter.api.*

class Mariadb_DbUtils_Test extends DbUtils_Test {

    @Test
    void test1() throws Exception {
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
