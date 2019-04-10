package jandcode.db.firebird;

import jandcode.db.*;
import jandcode.db.jdbc.*;
import jandcode.commons.*;
import jandcode.commons.error.*;
import org.apache.commons.vfs2.*;

import java.io.*;

public class FirebirdDbManagerService extends JdbcDbManagerService {

    public static final String P_EMPTY_DATABASE = "empty-database";

    public boolean existDatabase() throws Exception {
        String dbn = getDbSource().getProps().get(DbSourcePropsConsts.database);

        File f = new File(dbn);
        return f.exists() && f.isFile();
    }

    public void createDatabase() throws Exception {
        String dbn = getDbSource().getProps().get(DbSourcePropsConsts.database);

        if (existDatabase()) {
            throw new XError("Database {0} already exist", dbn);
        }

        FileObject fdst = UtFile.getFileObject(dbn);

        String fnSrc = getDbSource().getProps().get(P_EMPTY_DATABASE);
        FileObject fsrc = UtFile.getFileObject(fnSrc);

        fdst.copyFrom(fsrc, new AllFileSelector());
    }

    public void dropDatabase() throws Exception {
        if (!existDatabase()) {
            return;
        }
        String dbn = getDbSource().getProps().get(DbSourcePropsConsts.database);

        File f = new File(dbn);
        if (!f.delete()) {
            throw new XError("Database {0} not deleted", dbn);
        }

    }

}
