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
    private Map<String, ReplacePart> replacePart;

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

    static class ReplacePart {
        String name;
        List<String> parts;

        public ReplacePart(String name, List<String> parts) {
            this.name = name;
            if (UtString.empty(this.name)) {
                this.name = "default";
            }
            this.parts = new ArrayList<>();
            if (parts != null) {
                this.parts.addAll(parts);
            }
        }

        String replace(String sql) {
            if (this.parts.size() == 0) {
                return sql;
            }
            return SqlPartsUtils.replacePart(sql, this.name, this.parts);
        }

        public String getName() {
            return name;
        }

        public List<String> getParts() {
            return parts;
        }
    }

    //////

    public SqlTextImpl(Model model, String sql) {
        setModel(model);
        this.sql = sql;
    }

    //////

    public SqlTextImpl cloneSqlText() {
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

        if (this.replacePart != null) {
            for (var rw : this.replacePart.values()) {
                res.replacePart(rw.name, rw.parts);
            }
        }

        return res;
    }

    public SqlText asCountSqlText(String countFieldName) {
        SqlText res = cloneSqlText();
        res.paginate(false);
        res.replaceSelect("count(*) as " + countFieldName);
        res.replaceOrderBy("");
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
        if (this.replacePart != null) {
            for (ReplacePart rw : this.replacePart.values()) {
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

    public boolean isPaginate() {
        return this.paginate;
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

    public SqlText addWhere(String whereName, String whereText) {
        reset();
        if (UtString.empty(whereName)) {
            whereName = "default";
        }
        if (this.replaceWhere == null) {
            replaceWhere(whereName, whereText);
        } else {
            ReplaceWhere rw = this.replaceWhere.get(whereName);
            if (rw == null) {
                replaceWhere(whereName, whereText);
            } else {
                rw.wheres.add(whereText);
            }
        }
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

    //////

    public SqlText replacePart(String partName, List<String> partTexts) {
        reset();
        if (this.replacePart == null) {
            this.replacePart = new LinkedHashMap<>();
        }
        ReplacePart rw = new ReplacePart(partName, partTexts);
        this.replacePart.put(rw.name, rw);
        return this;
    }

    public SqlText addPart(String partName, String partText) {
        reset();
        if (UtString.empty(partName)) {
            partName = "default";
        }
        if (this.replacePart == null) {
            replacePart(partName, partText);
        } else {
            ReplacePart rw = this.replacePart.get(partName);
            if (rw == null) {
                replacePart(partName, partText);
            } else {
                rw.parts.add(partText);
            }
        }
        return this;
    }
}
