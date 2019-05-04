package jandcode.db.h2;

import jandcode.db.*;
import jandcode.db.std.*;

/**
 * DbManagerServive для h2 в памяти.
 * База данных всегда существует.
 */
public class H2MemDbManagerService extends BaseDbManagerService {

    public boolean existDatabase() throws Exception {
        Db dbsys = getDbSource().createDb(true);
        dbsys.connect();
        dbsys.disconnect();
        return true;
    }

    public void createDatabase() throws Exception {
        Db dbsys = getDbSource().createDb(true);
        dbsys.connect();
        dbsys.disconnect();
    }

    public void dropDatabase() throws Exception {
        Db dbsys = getDbSource().createDb(true);
        dbsys.connect();
        try {
            dbsys.execQuery("DROP ALL OBJECTS");
        } finally {
            dbsys.disconnect();
        }
    }

}
