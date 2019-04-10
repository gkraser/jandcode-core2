package jandcode.db.oracle;

import jandcode.db.*;
import jandcode.db.jdbc.*;

public class OracleDbMetadataService extends JdbcDbMetadataService {

    protected String getCurrentSchema(Db db) throws Exception {
        DbQuery q = db.openQuery("select user from dual");
        String un;
        try {
            un = q.getString("user");
        } finally {
            q.close();
        }
        return un;
    }

}
