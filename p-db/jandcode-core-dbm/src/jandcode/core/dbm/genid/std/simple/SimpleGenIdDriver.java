package jandcode.core.dbm.genid.std.simple;

import jandcode.commons.*;
import jandcode.commons.error.*;
import jandcode.commons.named.*;
import jandcode.core.db.*;
import jandcode.core.dbm.genid.*;
import jandcode.core.dbm.genid.impl.*;
import jandcode.core.dbm.genid.std.*;
import jandcode.core.dbm.mdb.*;
import jandcode.core.store.*;

public class SimpleGenIdDriver extends BaseGenIdDriver {

    public boolean isSupportUpdateCurrentId(GenId genId) {
        return true;
    }

    public boolean isSupportGenIdCache(GenId genId) {
        return true;
    }

    protected void doInitDriver() throws Exception {
        // забираем текущее состояние из базы данных
        Mdb mdb = getModel().createMdb();
        mdb.connect();
        try {
            NamedList<GenId> ids = getGenIds();
            Store st = mdb.loadQuery("select * from genid_data");
            for (StoreRecord r : st) {
                String gn = r.getString("name");
                GenIdImpl gid = (GenIdImpl) ids.find(gn);
                if (gid != null) {
                    // есть такой генератор, обновляем данные
                    gid.setStart(r.getLong("startId"));
                    gid.setStep(r.getLong("stepId"));
                }
            }
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
        String gn = genId.getName().toLowerCase();
        long res = 0;
        Mdb mdb = getModel().createMdb();
        mdb.connect();
        try {
            mdb.startTran();
            try {
                mdb.execQuery("update genid_data set val=val+stepId*" + count + " where name=:name",
                        UtCnv.toMap(
                                "name", gn
                        )
                );
                DbQuery q = mdb.openQuery("select val from genid_data where name=:name",
                        UtCnv.toMap(
                                "name", gn
                        )
                );
                try {
                    res = q.getLong(0);
                } finally {
                    q.close();
                }
                mdb.commit();
            } catch (Exception e) {
                mdb.rollback(e);
            }
        } finally {
            mdb.disconnect();
        }
        return res;
    }

    public long getNextId(GenId genId) throws Exception {
        return doGetNextIdCount(genId, 1);
    }

    public GenIdCache getGenIdCache(GenId genId, long count) throws Exception {
        initDriver();
        long lastId = doGetNextIdCount(genId, count);
        return new DefaultGenIdCache(lastId, count, genId.getStep());
    }

    public long getCurrentId(GenId genId) throws Exception {
        initDriver();
        String gn = genId.getName().toLowerCase();
        long res;
        Mdb mdb = getModel().createMdb();
        mdb.connect();
        try {
            DbQuery q = mdb.openQuery("select val from genid_data where name='" + gn + "'");
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
        String gn = genId.getName().toLowerCase();
        //
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
        //
        Mdb mdb = getModel().createMdb();
        mdb.connect();
        try {
            mdb.execQuery("update genid_data set val=:val where name=:name",
                    UtCnv.toMap(
                            "name", gn,
                            "val", v
                    )
            );
        } finally {
            mdb.disconnect();
        }
    }

}
