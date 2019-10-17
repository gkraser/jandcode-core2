package jandcode.core.db.mariadb;

import jandcode.core.db.*;
import jandcode.core.db.std.*;

public class MariadbDbManagerService extends BaseDbManagerService {

    public boolean existDatabase() throws Exception {
        boolean res = false;
        Db dbsys = getSystemDbSource().createDb(true);
        dbsys.connect();
        try {
            String dbn = getDbSource().getProps().getString(DbSourcePropsConsts.database);

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
        Db dbsys = getSystemDbSource().createDb(true);
        dbsys.connect();
        try {
            String dbn = getDbSource().getProps().getString(DbSourcePropsConsts.database);

            dbsys.execQuery("create database " +
                    dbn +
                    " DEFAULT CHARACTER SET utf8");
        } finally {
            dbsys.disconnect();
        }
    }

    public void dropDatabase() throws Exception {
        Db dbsys = getSystemDbSource().createDb(true);
        dbsys.connect();
        try {
            String dbn = getDbSource().getProps().getString(DbSourcePropsConsts.database);

            dbsys.execQuery("drop database " +
                    dbn);
        } finally {
            dbsys.disconnect();
        }
    }

}
