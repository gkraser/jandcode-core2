package jandcode.db.mysql;

import jandcode.core.test.*;
import jandcode.db.*;
import org.junit.jupiter.api.*;

public class MysqlConnectionService_Test extends App_Test {

    @Test
    public void test_disconnectAll() throws Exception {

        DbSource dbs = app.bean(DbService.class).getDbSource("test.mysql.create");
        DbManagerService man = dbs.bean(DbManagerService.class);

        if (man.existDatabase()) {
            man.dropDatabase();
        }

        if (!man.existDatabase()) {
            man.createDatabase();
        }

        Db db1 = dbs.createDb();
        db1.connect();

        db1.execQuery("create table t1(id varchar(10))");
        DbQuery q = db1.openQuery("select * from t1");
        q.close();

        db1.disconnect();

        try {
            System.out.println(dbs.bean(DbConnectionService.class));
            dbs.bean(DbConnectionService.class).disconnectAll();
            man.dropDatabase();
        } finally {
            db1.disconnect();
        }

        // after

        man.createDatabase();

        db1.connect();

        db1.execQuery("create table t1(id varchar(10))");
        q = db1.openQuery("select * from t1");
        q.close();

        db1.disconnect();

    }

    @Test
    public void test_connectDirect() throws Exception {
        DbSource dbs = app.bean(DbService.class).getDbSource("test.mysql.create");
        DbManagerService man = dbs.bean(DbManagerService.class);

        if (man.existDatabase()) {
            man.dropDatabase();
        }

        if (!man.existDatabase()) {
            man.createDatabase();
        }

        Db db1 = dbs.createDb();

        try {
            db1.connectDirect();
        } finally {
            db1.disconnect();
        }

    }


}
