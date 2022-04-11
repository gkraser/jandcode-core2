package jandcode.core.dbm.genid.std.mysql;

import jandcode.commons.error.*;
import jandcode.core.db.*;
import jandcode.core.dbm.genid.*;
import jandcode.core.dbm.genid.std.simple.*;
import jandcode.core.dbm.mdb.*;

public class MysqlGenIdDriver extends SimpleGenIdDriver {

    protected long doGetNextIdCount(GenId genId, long count) throws Exception {
        initDriver();
        if (count < 1) {
            throw new XError("count должен быть > 0");
        }
        String gn = genId.getName().toLowerCase();
        long res;
        Mdb mdb = getModel().createMdb();
        mdb.connect();
        try {
            DbQuery q = mdb.openQuery("select genid_nextid('" + gn + "', " + count + ")");
            try {
                res = q.getLong(0);
            } finally {
                q.close();
            }
        } finally {
            mdb.disconnect();
        }
        return res;
    }

}
