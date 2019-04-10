package jandcode.db.derby;

import jandcode.db.*;
import jandcode.db.jdbc.*;
import jandcode.commons.*;

import java.io.*;
import java.sql.*;

public class DerbyDbManagerService extends JdbcDbManagerService {

    public boolean existDatabase() throws Exception {
        String dbn = getDbSource().getProps().get(DbSourcePropsConsts.database);

        File f = new File(dbn + "/seg0");
        File f2 = new File(dbn + "/service.properties");
        return f.exists() && f.isDirectory() && f2.exists() && f2.isFile();
    }

    public void createDatabase() throws Exception {
        Db db = getSystemDb();
        DbSource dbs = db.getDbSource();
        String u = dbs.getProps().get("url") + ";create=true";
        dbs.getProps().put("url", u);
        try {
            // что бы создалась нужно соеденится
            db.connectDirect();
        } finally {
            db.disconnect();
        }
    }

    public void dropDatabase() throws Exception {
        if (!existDatabase()) {
            return;
        }
        Db db = getSystemDb();
        DbSource dbs = db.getDbSource();

        String url = dbs.getProps().get(DbSourcePropsConsts.url);

        try {
            DriverManager.getConnection(url + ";shutdown=true");
        } catch (SQLException ignore) {
        }
        //
        String dbn = db.getDbSource().getProps().get(DbSourcePropsConsts.database);

        File f = new File(dbn);
        UtFile.cleanDir(f);
        f.delete();
    }

}
