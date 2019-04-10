package jandcode.db.oracle;

import jandcode.core.test.*;
import jandcode.db.*;
import org.junit.jupiter.api.*;

public class OracleConnectionService_Test extends App_Test {

    @Test
    public void test_disconnectAll() throws Exception {

        DbSource dbs = app.bean(DbService.class).createDbSource("test.oracle.create");
        DbManagerService man = dbs.bean(DbManagerService.class);

        if (man.existDatabase()) {
            man.dropDatabase();
        }

        if (!man.existDatabase()) {
            man.createDatabase();
        }

        Db db1 = dbs.getDb();
        db1.connect();

        db1.execQuery("create table t1(id varchar(10))");
        DbQuery q = db1.openQuery("select * from t1");
        q.close();

        db1.disconnect();
        db1.getDbSource().bean(DbConnectionService.class).disconnectAll();


        try {
            System.out.println(dbs.bean(DbConnectionService.class));
            dbs.bean(DbConnectionService.class).disconnectAll();
            man.dropDatabase();
        } finally {
            db1.disconnect();
        }

        // after
        // если раскоментировать, ломается следущий тест
        // OracleDbManager_Test.test1()
/*
        man.createDatabase();

        db1.connect();

        db1.execQuery("create table t1(id varchar(10))");
        q = db1.openQuery("select * from t1");
        q.close();

        db1.disconnect();
*/
    }

}
