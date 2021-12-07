package jandcode.core.dbm.sql.impl;

import jandcode.commons.*;
import jandcode.core.dbm.*;
import jandcode.core.dbm.sql.*;

import java.util.*;

public class SqlTextImpl extends BaseModelMember implements SqlText {

    private String sql;
    private String sqlPrepared;

    private boolean paginate;
    private String paginateParamsPrefix;

    private Map<String, ReplaceWhere> replaceWhere;
    private ReplaceSelect replaceSelect;
    private ReplaceOrderBy replaceOrderBy;

    static class ReplaceWhere {
        String name;
        List<String> wheres;

        public ReplaceWhere(String name, List<String> wheres) {
            this.name = name;
            if (UtString.empty(this.name)) {
                this.name = "default";
            }
            this.wheres = new ArrayList<>();
            if (wheres != null) {
                this.wheres.addAll(wheres);
            }
        }

        String replace(String sql) {
            if (this.wheres.size() == 0) {
                return sql;
            }
            return SqlPartsUtils.replaceWhere(sql, this.name, this.wheres);
        }

        public String getName() {
            return name;
        }

        public List<String> getWheres() {
            return wheres;
        }
    }

    static class ReplaceSelect {
        String text;
        boolean append;

        public ReplaceSelect(String text, boolean append) {
            this.text = text;
            this.append = append;
        }

        String replace(String sql) {
            if (UtString.empty(this.text)) {
                return sql;
            }
            return SqlPartsUtils.replaceSelect(sql, this.text, this.append);
        }

    }

    static class ReplaceOrderBy {
        String text;

        public ReplaceOrderBy(String text) {
            this.text = text;
        }

        String replace(String sql) {
            return SqlPartsUtils.replaceOrderBy(sql, this.text);
        }

    }

    //////

    public SqlTextImpl(Model model, String sql) {
        setModel(model);
        this.sql = sql;
    }

    //////

    public SqlText cloneSqlText() {
        SqlTextImpl res = new SqlTextImpl(getModel(), this.sql);

        res.paginate = this.paginate;
        res.paginateParamsPrefix = this.paginateParamsPrefix;
        res.replaceSelect = this.replaceSelect;
        res.replaceOrderBy = this.replaceOrderBy;

        if (this.replaceWhere != null) {
            for (var rw : this.replaceWhere.values()) {
                res.replaceWhere(rw.name, rw.wheres);
            }
        }

        return res;
    }

    //////

    private void reset() {
        this.sqlPrepared = null;
    }

    private String prepareSql() {
        String res = this.sql;

        if (this.replaceWhere != null) {
            for (ReplaceWhere rw : this.replaceWhere.values()) {
                res = rw.replace(res);
            }
        }
        if (this.replaceSelect != null) {
            res = this.replaceSelect.replace(res);
        }
        if (this.replaceOrderBy != null) {
            res = this.replaceOrderBy.replace(res);
        }

        if (this.paginate) {
            SqlPaginate pgImpl = getModel().bean(SqlPaginate.class);
            res = pgImpl.paginate(res, this.paginateParamsPrefix);
        }

        return res;
    }

    ////// CharSequence

    public int length() {
        return getSql().length();
    }

    public char charAt(int index) {
        return getSql().charAt(index);
    }

    public CharSequence subSequence(int start, int end) {
        return getSql().subSequence(start, end);
    }

    //////

    public String getOrigSql() {
        return sql;
    }

    public void setSql(String sql) {
        reset();
        this.sql = sql;
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

    public SqlText paginate(boolean v) {
        reset();
        this.paginate = v;
        return this;
    }

    public SqlText paginateParamsPrefix(String prefix) {
        reset();
        this.paginateParamsPrefix = prefix;
        return this;
    }

    //////

    public SqlText replaceWhere(String whereName, List<String> whereTexts) {
        reset();
        if (this.replaceWhere == null) {
            this.replaceWhere = new LinkedHashMap<>();
        }
        ReplaceWhere rw = new ReplaceWhere(whereName, whereTexts);
        this.replaceWhere.put(rw.name, rw);
        return this;
    }

    public SqlText replaceSelect(String text, boolean append) {
        reset();
        this.replaceSelect = new ReplaceSelect(text, append);
        return this;
    }

    public SqlText replaceOrderBy(String text) {
        reset();
        this.replaceOrderBy = new ReplaceOrderBy(text);
        return this;
    }

}
