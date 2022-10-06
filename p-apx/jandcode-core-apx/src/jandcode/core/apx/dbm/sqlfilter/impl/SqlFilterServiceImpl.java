package jandcode.core.apx.dbm.sqlfilter.impl;

import jandcode.commons.conf.*;
import jandcode.commons.named.*;
import jandcode.core.*;
import jandcode.core.apx.dbm.sqlfilter.*;
import jandcode.core.dbm.*;

import java.util.*;

public class SqlFilterServiceImpl extends BaseModelMember implements SqlFilterService {

    private NamedList<SqlFilterBuilderDef> sqlFilterBuilderDefs = new DefaultNamedList<>("sql-filter-builder [{0}] не найден");

    class SqlFilterBuilderDef extends Named {

        Conf conf;

        public SqlFilterBuilderDef(Conf conf) {
            this.conf = conf;
            setName(conf.getName());
        }

        public SqlFilterBuilder createInst() {
            return (SqlFilterBuilder) getModel().create(conf);
        }

    }

    protected void onConfigure(BeanConfig cfg) throws Exception {
        super.onConfigure(cfg);

        //
        for (Conf conf : getModel().getConf().getConfs("sql-filter-builder")) {
            SqlFilterBuilderDef f = new SqlFilterBuilderDef(conf);
            this.sqlFilterBuilderDefs.add(f);
        }
    }

    public List<String> getSqlFilterBuilderNames() {
        return sqlFilterBuilderDefs.getNames();
    }

    public SqlFilterBuilder createSqlFilterBuilder(String name) {
        return sqlFilterBuilderDefs.get(name).createInst();
    }

}
