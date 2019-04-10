package jandcode.db.impl;

import jandcode.core.*;
import jandcode.db.*;
import jandcode.commons.*;
import jandcode.commons.error.*;
import jandcode.commons.named.*;
import jandcode.commons.conf.*;
import jandcode.commons.variant.*;

import java.sql.*;
import java.util.*;

public class DbDriverImpl extends BaseDbSourceMember implements DbDriver {

    private String dbType;
    private NamedList<DbDatatype> dbDatatypes = new DefaultNamedList<>();
    private ClassLinks<String> dbDatatypesByClass = new ClassLinks<>();
    private List<String> initConnectionSqls;

    //////

    protected void onConfigure(BeanConfig cfg) throws Exception {
        super.onConfigure(cfg);
        //
        Conf conf = cfg.getConf();
        //
        for (Conf x : conf.getConfs("dbdatatype")) {
            addDbDatatype(x);
        }
        //
        for (Conf x : conf.getConfs("type")) {
            addType(x);
        }
    }

    protected void addDbDatatype(Conf x) {
        DbDatatype r = (DbDatatype) getDbSource().create(x);
        dbDatatypes.add(r);
    }

    protected void addType(Conf x) {
        String rn = x.getString("dbdatatype");
        if (UtString.empty(rn)) {
            return; // пропускаем, видимо для чего-то еще нужен
        }
        dbDatatypesByClass.add(x.getName(), rn);
    }

    //////

    public String getDbType() {
        return dbType;
    }

    public void setDbType(String dbType) {
        this.dbType = dbType;
    }

    //////

    public NamedList<DbDatatype> getDbDatatypes() {
        return dbDatatypes;
    }

    protected ClassLinks<String> getDbDatatypesByClass() {
        return dbDatatypesByClass;
    }

    /**
     * Для sql типа возвращает имя dbdatatype
     */
    protected String getDbDatatypeName(int sqltype) {
        switch (sqltype) {

            case Types.BIT:
            case Types.BOOLEAN:
            case Types.TINYINT:
            case Types.SMALLINT:
                return "smallint";

            case Types.INTEGER:
                return "int";

            case Types.BIGINT:
                return "long";

            case Types.FLOAT:
            case Types.REAL:
            case Types.DOUBLE:
            case Types.NUMERIC:
            case Types.DECIMAL:
                return "double";

            case Types.CHAR:
            case Types.VARCHAR:
            case Types.NCHAR:
            case Types.NVARCHAR:
            case Types.OTHER:
                return "string";

            case Types.CLOB:
            case Types.NCLOB:
            case Types.LONGVARCHAR:
            case Types.LONGNVARCHAR:
                return "memo";

            case Types.DATE:
                return "date";

            case Types.TIME:
            case Types.TIMESTAMP:
                return "datetime";

            case Types.BLOB:
            case Types.BINARY:
            case Types.VARBINARY:
            case Types.LONGVARBINARY:
                return "blob";

            case Types.JAVA_OBJECT:
                return "object";

            default:
                throw new XError("For java.sql.Types=[{0}] not found dbdatatype", sqltype);
        }
    }

    /**
     * Для колонки возвращает имя dbdatatype
     */
    protected String getDbDatatypeName(ResultSetMetaData md, int colIdx) throws Exception {
        int ct = md.getColumnType(colIdx);
        String dt = getDbDatatypeName(ct);
        return dt;
    }

    /**
     * dbdatatype для значения
     */
    protected DbDatatype getDbDatatype(Object value) {
        String cn = "null";
        if (value != null) {
            cn = value.getClass().getName();
        }
        String nm = getDbDatatypesByClass().get(cn);
        if (nm == null) {
            throw new XError("Не определен dbdatatype для класса {0}", cn);
        }
        return getDbDatatypes().get(nm);
    }

    public DbDatatype getDbDatatype(ResultSetMetaData md, int colIdx) throws Exception {
        String nm = getDbDatatypeName(md, colIdx);
        return getDbDatatypes().get(nm);
    }

    public DbDatatype getDbDatatype(int sqlType) throws Exception {
        String nm = getDbDatatypeName(sqlType);
        return getDbDatatypes().get(nm);
    }

    //////

    /**
     * Присвоить null-параметр
     */
    protected void setNullParam(PreparedStatement statement, int paramIdx, VariantDataType datatype) throws Exception {
        statement.setObject(paramIdx, null);
    }

    public void assignStatementParams(PreparedStatement statement, IVariantNamed paramValues, List<String> paramNames) throws Exception {
        for (int i = 0; i < paramNames.size(); i++) {
            String paramName = paramNames.get(i);
            int paramIdx = i + 1;

            VariantDataType dt = paramValues.getDataType(paramName);

            if (paramValues.isNull(paramName)) {
                setNullParam(statement, paramIdx, dt);
            } else {
                Object value = paramValues.getValue(paramName);
                DbDatatype dbdt = getDbDatatype(value);
                dbdt.setValue(statement, paramIdx, value);
            }
        }
    }

    public List<String> getInitConnectionSqls() {
        if (initConnectionSqls == null) {
            synchronized (this) {
                if (initConnectionSqls == null) {
                    initConnectionSqls = grabInitConnectionSqls();
                }
            }
        }
        return initConnectionSqls;
    }

    protected List<String> grabInitConnectionSqls() {
        List<String> res = new ArrayList<>();
        Map<String, String> p = getDbSource().getProps(DbSourcePropsConsts.initConnectionSql, false);
        TreeMap<String, String> m = new TreeMap<>(p);
        for (String s : m.values()) {
            res.add(s);
        }
        return res;
    }

}
