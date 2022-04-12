package jandcode.core.dbm.genid.std.oracle;

import jandcode.commons.*;
import jandcode.commons.named.*;
import jandcode.core.db.*;
import jandcode.core.dbm.genid.*;
import jandcode.core.dbm.genid.impl.*;
import jandcode.core.dbm.genid.std.*;
import jandcode.core.dbm.mdb.*;
import jandcode.core.store.*;

/**
 * Простой oracle genid-драйвер.
 * Работает через sequence.
 * Не поддерживает update и cache.
 */
public class OracleGenIdDriver extends BaseGenIdDriver {

    public static String seqPrefix = "g_";


    protected void doInitDriver() throws Exception {
        // загрузить настоящие step
        Mdb mdb = getModel().createMdb();
        mdb.connect();
        try {
            NamedList<GenId> ids = getGenIds();
            Store st = mdb.loadQuery("select user_sequences.* from user_sequences");
            for (StoreRecord r : st) {
                String seqName = r.getString("sequence_name").toLowerCase();
                String gn = UtString.removePrefix(seqName, seqPrefix);
                if (gn == null) {
                    continue; // не наше
                }
                GenIdImpl gid = (GenIdImpl) ids.find(gn);
                if (gid != null) {
                    // есть такой генератор, обновляем данные
                    gid.setStep(r.getLong("increment_by"));
                }
            }
        } finally {
            mdb.disconnect();
        }
    }

    public long getNextId(GenId genId) throws Exception {
        initDriver();
        long res;
        Mdb mdb = genId.getMdb();
        mdb.connect();
        try {
            String s = seqPrefix + genId.getName() + ".nextval";
            DbQuery q = mdb.openQuery("select " + s + " from dual");
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

    public long getCurrentId(GenId genId) throws Exception {
        initDriver();
        long res;
        Mdb mdb = genId.getMdb();
        mdb.connect();
        try {
            String s = seqPrefix + genId.getName();
            s = s.toUpperCase();
            DbQuery q = mdb.openQuery("select last_number from user_sequences where sequence_name=:id",
                    s);
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
