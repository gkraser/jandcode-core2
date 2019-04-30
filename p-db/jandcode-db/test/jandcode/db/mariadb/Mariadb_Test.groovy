package jandcode.db.mariadb

import jandcode.core.test.*
import jandcode.db.test.*
import org.junit.jupiter.api.*

class Mariadb_Test extends App_Test {

    DbSimpleTestSvc dbSimple = testSvc(DbSimpleTestSvc)

    @Test
    public void sqltypes() throws Exception {
        dbSimple.checkSqlTypes()
    }

}
