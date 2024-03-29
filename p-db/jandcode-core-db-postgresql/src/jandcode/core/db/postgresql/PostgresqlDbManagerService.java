package jandcode.core.db.postgresql;

import jandcode.core.db.*;
import jandcode.core.db.std.*;

public class PostgresqlDbManagerService extends BaseDbManagerService {

    public boolean existDatabase() throws Exception {
        boolean res = false;
        Db dbsys = getSystemDbSource().createDb(true);
        dbsys.connect();
        try {
            String dbn = getDbSource().getProps().getString(DbSourcePropsConsts.database);

            DbQuery q = dbsys.openQuery("SELECT datname FROM pg_database WHERE upper(datname)=upper(:id)", dbn);
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
                    " encoding 'UTF8'");
        } finally {
            dbsys.disconnect();
        }
    }

    public void dropDatabase() throws Exception {
        Db dbsys = getSystemDbSource().createDb(true);
        dbsys.connect();
        try {
            String dbn = getDbSource().getProps().getString(DbSourcePropsConsts.database);

            boolean force = getDbSource().getProps().getBoolean("dropdatabase.force", getApp().getEnv().isDev());
            if (force) {
                dbsys.execQueryNative(
                        "update pg_database set datallowconn=false where lower(datname)='" + dbn.toLowerCase() + "'"
                );
                dbsys.execQueryNative(
                        "select pg_terminate_backend(pid) from pg_stat_activity where lower(datname)='" + dbn.toLowerCase() + "'"
                );
                dbsys.execQueryNative(
                        "drop database " + dbn + " with (force)"
                );
            } else {
                dbsys.execQuery("drop database " + dbn);
            }
        } finally {
            dbsys.disconnect();
        }
    }

}
