package jandcode.db.mysql;

import jandcode.db.*;
import jandcode.db.jdbc.*;

public class MysqlDbManagerService extends JdbcDbManagerService {

    public boolean existDatabase() throws Exception {
        boolean res = false;
        Db dbsys = getSystemDb();
        dbsys.connectDirect();
        try {
            String dbn = getDbSource().getProps().get(DbSourcePropsConsts.database);

            DbQuery q = dbsys.openQuery("SELECT SCHEMA_NAME FROM INFORMATION_SCHEMA.SCHEMATA WHERE SCHEMA_NAME=:id", dbn);
            try {
                res = !q.eof();
            } finally {
                q.close();
            }
        } finally {
            dbsys.disconnect();
        }
        return res;
    }

    public void createDatabase() throws Exception {
        Db dbsys = getSystemDb();
        dbsys.connectDirect();
        try {
            String dbn = getDbSource().getProps().get(DbSourcePropsConsts.database);

            dbsys.execQuery("create database " +
                    dbn +
                    " DEFAULT CHARACTER SET utf8");
        } finally {
            dbsys.disconnect();
        }
    }

    public void dropDatabase() throws Exception {
        Db dbsys = getSystemDb();
        dbsys.connectDirect();
        try {
            String dbn = getDbSource().getProps().get(DbSourcePropsConsts.database);

            dbsys.execQuery("drop database " +
                    dbn);
        } finally {
            dbsys.disconnect();
        }
    }

}
