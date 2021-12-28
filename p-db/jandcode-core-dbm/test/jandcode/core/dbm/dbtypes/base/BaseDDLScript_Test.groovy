package jandcode.core.dbm.dbtypes.base

import jandcode.core.db.*
import jandcode.core.dbm.ddl.*
import jandcode.core.dbm.test.*
import org.junit.jupiter.api.*

abstract class BaseDDLScript_Test extends Dbm_Test {

    protected void recreateDb() {
        dbm.showDb()
        dbm.db.dbSource.disconnectAll()
        def dbManSvc = model.dbSource.bean(DbManagerService)
        if (dbManSvc.existDatabase()) {
            println "DROP"
            dbManSvc.dropDatabase()
        }
        println "CREATE"
        dbManSvc.createDatabase()
    }

    @Test
    public void exec_script() throws Exception {
        recreateDb()
        //
        def svc = model.bean(DDLService)
        def script = svc.grabScript()
        utils.logOn()
        utils.delim("start script")
        script.exec(mdb)
        utils.delim("stop script")
    }


}
