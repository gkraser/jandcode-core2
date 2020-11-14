package jandcode.core.db.oracle;


import jandcode.core.db.*;
import jandcode.core.db.std.*;

public class OracleDbMetadataService extends BaseDbMetadataService {

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
