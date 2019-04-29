package jandcode.db.mariadb

import jandcode.core.test.*
import jandcode.db.*
import org.junit.jupiter.api.*

class Mariadb_Test extends App_Test {

    DbService svc;

    public void setUp() throws Exception {
        super.setUp();
        //
        svc = app.bean(DbService.class);
    }

    @Test
    public void test1() throws Exception {
        DbSource dbs = svc.getDbSource("test1")
        utils.outMap(dbs.props)
        //
        Db db = dbs.createDb(true)
        //
        db.connect()
        db.disconnect()
        db.connect()
        db.disconnect()
        //
    }


}
