package jandcode.core.dbm.sql.impl;

import jandcode.core.dbm.*;
import jandcode.core.dbm.sql.*;

public class SqlUtilsImpl implements SqlUtils {

    private Model model;
    private ISqlPaginate sqlPaginate;

    public SqlUtilsImpl(Model model) {
        this.model = model;

    }

    ////// paginate

    private ISqlPaginate getSqlPaginate() {
        if (sqlPaginate == null) {
            sqlPaginate = model.bean(ISqlPaginate.class);
        }
        return sqlPaginate;
    }

    public String paginate(String srcSql, long offset, long limit) {
        return getSqlPaginate().paginate(srcSql, offset, limit);
    }

    public String paginate(String srcSql, String paramsPrefix) {
        return getSqlPaginate().paginate(srcSql, paramsPrefix);
    }

}
