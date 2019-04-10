package jandcode.db.mysql.validationsql

import jandcode.core.test.*
import jandcode.db.*
import org.junit.jupiter.api.*

/**
 * Проверка на наличие validationQuery. Ручками выполняем и смотрим лог в mysql...
 */
class ValidationSql_Test extends App_Test {

    Db db

    void setUp() throws Exception {
        super.setUp()
        //
        def svc = app.bean(DbService)
        DbSource dbs = svc.getDbSource("test.1")
        db = dbs.createDb()
    }

    @Test
    public void test_create() throws Exception {
        def svc = db.dbSource.bean(DbManagerService)
        if (!svc.existDatabase()) {
            svc.createDatabase()
        }
    }

    @Test
    public void test1() throws Exception {
        db.connect()
        DbQuery q = db.openQuery("select 2+5 as q")
        println q.getValue("q")
        q.close()
        db.disconnect()
    }


}
