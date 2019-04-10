package jandcode.db.postgresql;

import jandcode.db.*;
import jandcode.db.jdbc.*;

public class PostgresqlDbManagerService extends JdbcDbManagerService {

    public boolean existDatabase() throws Exception {
        boolean res = false;
        Db dbsys = getSystemDb();
        dbsys.connectDirect();
        try {
            String dbn = getDbSource().getProps().get(DbSourcePropsConsts.database);

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
        Db dbsys = getSystemDb();
        dbsys.connectDirect();
        try {
            String dbn = getDbSource().getProps().get(DbSourcePropsConsts.database);

            dbsys.execQuery("create database " +
                    dbn +
                    " encoding 'UTF8'");
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
