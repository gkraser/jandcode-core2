package jandcode.core.apx.dbm.sqlfilter.impl;

import jandcode.commons.conf.*;
import jandcode.commons.error.*;
import jandcode.commons.named.*;
import jandcode.core.*;
import jandcode.core.dbm.*;
import jandcode.core.dbm.sql.*;

import java.util.*;

public class SqlServiceImpl extends BaseModelMember implements SqlService {

    private NamedList<SqlFilterWhereDef> sqlFilterWhereDefs = new DefaultNamedList<>("sql-filter-where [{0}] не найден");

    protected void onConfigure(BeanConfig cfg) throws Exception {
        super.onConfigure(cfg);

        //
        for (Conf conf : getModel().getConf().getConfs("sql-filter-where")) {
            SqlFilterWhereDef f = new SqlFilterWhereDefImpl(getModel(), conf.getName(), conf);
            this.sqlFilterWhereDefs.add(f);
        }
    }

    public SqlText createSqlText(String sql) {
        return new SqlTextImpl(getModel(), sql);
    }

    public SqlText createSqlText(Conf conf, Object context) {
        try {
            String sql = SqlConfUtils.loadSqlTextFromConf(conf, getModel(), context);
            return createSqlText(sql);
        } catch (Exception e) {
            throw new XErrorMark(e, conf.origin().toString());
        }
    }

    public SqlBuilder createSqlBuilder() {
        return getModel().create(SqlBuilder.class);
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
