package jandcode.db.mysql

import jandcode.core.test.*
import jandcode.db.*
import jandcode.commons.*
import jandcode.commons.datetime.XDateTime
import org.junit.jupiter.api.*

class MysqlServerTimezone_Test extends App_Test {

    Db db

    void setUp() throws Exception {
        super.setUp()
        //
        def svc = app.bean(DbService)
        DbSource dbs = svc.getDbSource("test.1")
        db = dbs.createDb()

        //
        def svcM = db.dbSource.bean(DbManagerService)
        if (!svcM.existDatabase()) {
            svcM.createDatabase()
        }

    }

    void tearDown() throws Exception {
        super.tearDown()
        if (db.isConnected()) {
            db.disconnect()
        }
    }

    @Test
    public void test1() throws Exception {
        db.connect()
        try {
            db.execQuery("drop table date1")
        } catch (e) {
            //ignore
        }
        db.execQuery("create table date1 (id bigint, dt datetime, s1 varchar(50))")
        XDateTime d = UtDateTime.create("2001-11-29T11:22:33")
        for (i in 1..10) {
            d = d.addDays(1)
            db.execQuery("insert into date1 (id,dt,s1) values (:id,:dt,:s1)", [id: i, dt: d, s1: d.toString()])
        }
        //
        def q = db.openQuery("select * from date1")
        while (!q.eof()) {
            println q.getDateTime("dt").toString() + " = " + q.getString("s1")
            q.next()
        }

    }


}
