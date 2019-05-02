package jandcode.db.mariadb

import jandcode.db.test.*
import org.junit.jupiter.api.*

import static org.junit.jupiter.api.Assertions.*

class Mariadb_DbDatatType_Test extends DbDatatypeTestCase {

    @Test
    public void test1() throws Exception {
        //sqltypes()
        storetypes()
        //test_memo()

        //assertEquals(z.dbdatatypeResult("memo"), "memo")

        assertEquals(z.dbdatatypeResult("datetime"), "datetime")


    }

}
