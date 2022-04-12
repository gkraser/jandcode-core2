package jandcode.core.dbm.genid.std.postgresql;

import jandcode.commons.*;
import jandcode.commons.error.*;
import jandcode.commons.named.*;
import jandcode.core.db.*;
import jandcode.core.dbm.genid.*;
import jandcode.core.dbm.genid.impl.*;
import jandcode.core.dbm.genid.std.*;
import jandcode.core.dbm.mdb.*;
import jandcode.core.store.*;

/**
 * postgresql genid-драйвер.
 * Работает через sequence.
 */
public class PostgresqlGenIdDriver extends BaseGenIdDriver {

    public static String seqPrefix = "g_";

    public boolean isSupportUpdateCurrentId(GenId genId) {
        return true;
    }

    public boolean isSupportGenIdCache(GenId genId) {
        return true;
    }

    protected void doInitDriver() throws Exception {
        // загрузить настоящие step
        Mdb mdb = getModel().createMdb();
        mdb.connect();
        try {
            NamedList<GenId> ids = getGenIds();
            Store st = mdb.loadQuery("select * from pg_sequences");
            for (StoreRecord r : st) {
                String seqName = r.getString("sequencename").toLowerCase();
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
        Mdb mdb = getModel().createMdb();
        mdb.connect();
        try {
            String s = seqPrefix + genId.getName();
            DbQuery q = mdb.openQuery("select nextval('" + s + "')");
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
        Mdb mdb = getModel().createMdb();
        mdb.connect();
        try {
            String s = seqPrefix + genId.getName();
            DbQuery q = mdb.openQuery("select last_value from " + s);
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

    public void updateCurrentId(GenId genId, long value) throws Exception {
        initDriver();

        long start = genId.getStart();
        long step = genId.getStep();
        //
        long v;
        if (value < start) {
            v = start;
        } else {
            v = (value - start) / step * step + start;
            if (v < value) {
                v = v + step;
            }
        }

        Mdb mdb = getModel().createMdb();
        mdb.connect();
        try {
            String s = seqPrefix + genId.getName();
            mdb.execQuery("select setval('" + s + "'," + v + ")");
        } finally {
            mdb.disconnect();
        }
    }

    /**
     * Загружает следующе так, как будто count выдано.
     */
    protected long doGetNextIdCount(GenId genId, long count) throws Exception {
        initDriver();
        if (count < 1) {
            throw new XError("count должен быть > 0");
        }
        String gn = seqPrefix + genId.getName();
        long res = 0;
        Mdb mdb = getModel().createMdb();
        mdb.connect();
        try {
            DbQuery q = mdb.openQuery("select setval('" + gn + "', nextval('" + gn +
                    "')+" + count * genId.getStep() + ")");
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

    public GenIdCache getGenIdCache(GenId genId, long count) throws Exception {
        initDriver();
        long lastId = doGetNextIdCount(genId, count);
        return new DefaultGenIdCache(lastId, count, genId.getStep());
    }

}
