package jandcode.core.dbm.domain.db.impl;

import jandcode.commons.conf.*;
import jandcode.core.dbm.domain.*;
import jandcode.core.dbm.domain.db.*;

import java.util.*;

public class DomainDbImpl extends BaseDomainMember implements DomainDb {

    private boolean dbTable;
    private boolean external;
    private List<DomainDbIndex> indexes;

    protected void onConfigureMember() throws Exception {
        dbTable = getDomain().hasTagDb();
//todo conf        external = !getModel().isDefinedForDbStruct(getDomain().getConf().getPath());
        external = !getModel().isDefinedForDbStruct(""); //todo conf
    }

    public boolean isDbTable() {
        return dbTable;
    }

    public boolean isExternal() {
        return external;
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
