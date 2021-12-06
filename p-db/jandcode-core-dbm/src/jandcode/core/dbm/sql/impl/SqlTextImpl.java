package jandcode.core.dbm.sql.impl;

import jandcode.core.dbm.*;
import jandcode.core.dbm.sql.*;

public class SqlTextImpl extends BaseModelMember implements SqlText {

    private String sql;
    private String sqlPrepared;

    private boolean paginate;
    private String paginateParamsPrefix;

    //////

    private void reset() {
        this.sqlPrepared = null;
    }

    private String prepareSql() {
        String res = this.sql;

        if (this.paginate) {
            ISqlPaginate pgImpl = getModel().bean(ISqlPaginate.class);
            res = pgImpl.paginate(res, this.paginateParamsPrefix);
        }

        return res;
    }

    //////

    public String getOrigSql() {
        return sql;
    }

    public void setSql(String sql) {
        this.sql = sql;
        reset();
    }

    public String getSql() {
        if (this.sqlPrepared == null) {
            this.sqlPrepared = prepareSql();
        }
        return this.sqlPrepared;
    }

    public String toString() {
        return getSql();
    }

    //////

    public void paginate(boolean v) {
        setPaginate(v);
    }

    public void setPaginate(boolean v) {
        this.paginate = v;
        reset();
    }

    public void paginateParamsPrefix(String prefix) {
        this.paginateParamsPrefix = prefix;
        reset();
    }

}
