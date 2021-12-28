package jandcode.core.dbm.dbstruct.impl;

import jandcode.commons.conf.*;
import jandcode.core.dbm.dbstruct.*;
import jandcode.core.dbm.domain.*;

import java.util.*;

public class DomainDbImpl extends BaseDomainMember implements DomainDb {

    private boolean dbTable;
    private boolean dbExternal;
    private List<DomainDbIndex> indexes;

    protected void onConfigureMember() throws Exception {
        Conf conf = getDomain().getConf();
        dbTable = getDomain().hasTag("db");
        dbExternal = conf.getBoolean("tag.dbexternal");
    }

    public boolean isDbTable() {
        return dbTable;
    }

    public boolean isDbExternal() {
        return dbExternal;
    }

    public List<DomainDbIndex> getIndexes() {
        if (indexes == null) {
            indexes = createIndexes();
        }
        return indexes;
    }

    protected List<DomainDbIndex> createIndexes() {
        List<DomainDbIndex> res = new ArrayList<>();
        for (Conf x : getDomain().getConf().getConfs("dbindex")) {
            DomainDbIndex it = getModel().create(x, DomainDbIndexImpl.class, false);
            res.add(it);
        }
        return res;
    }

    public long getGenIdStart() {
        return getDomain().getConf().getLong("genid.start", 1000);
    }

    public long getGenIdStep() {
        return getDomain().getConf().getLong("genid.step", 1);
    }

}
