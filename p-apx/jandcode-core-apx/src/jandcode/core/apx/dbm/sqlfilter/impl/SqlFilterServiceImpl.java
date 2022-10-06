package jandcode.core.apx.dbm.sqlfilter.impl;

import jandcode.commons.conf.*;
import jandcode.commons.named.*;
import jandcode.core.*;
import jandcode.core.apx.dbm.sqlfilter.*;
import jandcode.core.dbm.*;

import java.util.*;

public class SqlFilterServiceImpl extends BaseModelMember implements SqlFilterService {

    private NamedList<SqlFilterWhereDef> sqlFilterWhereDefs = new DefaultNamedList<>("sql-filter-where [{0}] не найден");

    protected void onConfigure(BeanConfig cfg) throws Exception {
        super.onConfigure(cfg);

        //
        for (Conf conf : getModel().getConf().getConfs("sql-filter-where")) {
            SqlFilterWhereDef f = new SqlFilterWhereDefImpl(getModel(), conf.getName(), conf);
            this.sqlFilterWhereDefs.add(f);
        }
    }

    public SqlFilter createSqlFilter(String sql, Map params) {
        return new SqlFilterImpl(getModel(), sql, params);
    }

    public NamedList<SqlFilterWhereDef> getSqlFilterWhereDefs() {
        return sqlFilterWhereDefs;
    }

    public SqlFilterWhere createSqlFilterWhere(String name) {
        return sqlFilterWhereDefs.get(name).createInst();
    }

}
