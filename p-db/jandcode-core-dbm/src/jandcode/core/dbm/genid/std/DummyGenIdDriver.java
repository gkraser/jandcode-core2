package jandcode.core.dbm.genid.std;


import jandcode.core.db.*;
import jandcode.core.dbm.genid.*;
import jandcode.core.dbm.mdb.*;

/**
 * dummy драйвер genid.
 * Только что бы было что то по умолчанию.
 * Не используейте в production!
 */
public class DummyGenIdDriver extends BaseGenIdDriver {

    protected void doInitDriver() throws Exception {
    }

    protected long loadMaxId(GenId genId) throws Exception {
        long res = 0;
        Mdb mdb = genId.getMdb();
        mdb.connect();
        try {
            DbQuery q = mdb.openQuery("select max(id) from " + genId.getName());
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

    public long getNextId(GenId genId) throws Exception {
        return loadMaxId(genId) + genId.getStep();
    }

    public long getCurrentId(GenId genId) throws Exception {
        return loadMaxId(genId);
    }

}
