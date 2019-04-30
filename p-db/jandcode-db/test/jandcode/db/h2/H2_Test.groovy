package jandcode.db.h2

import jandcode.core.test.*
import jandcode.db.test.*
import org.junit.jupiter.api.*

class H2_Test extends App_Test {

    DbSimpleTestSvc dbSimple = testSvc(DbSimpleTestSvc)

    @Test
    public void sqltypes() throws Exception {
        dbSimple.checkSqlTypes()
    }


}
