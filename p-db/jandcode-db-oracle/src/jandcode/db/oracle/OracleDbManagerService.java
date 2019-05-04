package jandcode.db.oracle;

import jandcode.db.*;
import jandcode.db.std.*;

public class OracleDbManagerService extends BaseDbManagerService {

    public boolean existDatabase() throws Exception {
        boolean res = false;
        Db dbsys = getSystemDbSource().createDb(true);
        dbsys.connect();
        try {
            String un = getDbSource().getProps().getString(DbSourcePropsConsts.username);
            DbQuery q = dbsys.openQuery("select username from all_users where username=upper(:id)", un);
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
            String un = getDbSource().getProps().getString(DbSourcePropsConsts.username);
            String ps = getDbSource().getProps().getString(DbSourcePropsConsts.password);

            dbsys.execQuery("create user " +
                    un +
                    " IDENTIFIED BY \"" + ps +
                    "\" DEFAULT TABLESPACE \"USERS\"");
            //todo oracle: очччень подозрительно!
            dbsys.execQuery("GRANT \"DBA\" TO " + un);
            dbsys.execQuery("GRANT EXECUTE ON DBMS_LOCK TO " + un);
        } finally {
            dbsys.disconnect();
        }
    }

    public void dropDatabase() throws Exception {
        Db dbsys = getSystemDbSource().createDb(true);
        dbsys.connect();
        try {
            String un = getDbSource().getProps().getString(DbSourcePropsConsts.username);

            dbsys.execQuery("drop user " +
                    un +
                    " cascade");
        } finally {
            dbsys.disconnect();
        }
    }

}
