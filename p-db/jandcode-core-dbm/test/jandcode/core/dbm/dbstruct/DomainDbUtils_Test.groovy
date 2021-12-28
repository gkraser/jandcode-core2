package jandcode.core.dbm.dbstruct

import jandcode.core.dbm.test.*
import org.junit.jupiter.api.*

import static org.junit.jupiter.api.Assertions.*;

class DomainDbUtils_Test extends Dbm_Test {

    @Test
    public void dbtables() throws Exception {
        def z = new DomainDbUtils(model)
        def tabs = z.getDbTables()
        println tabs.size()
        assertTrue(tabs.size() >= 2)
    }


}
