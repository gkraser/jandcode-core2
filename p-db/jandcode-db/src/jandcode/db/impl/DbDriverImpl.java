package jandcode.db.impl;

import jandcode.commons.*;
import jandcode.commons.conf.*;
import jandcode.commons.error.*;
import jandcode.commons.named.*;
import jandcode.commons.variant.*;
import jandcode.core.*;
import jandcode.db.*;

import java.sql.*;
import java.util.*;

public class DbDriverImpl extends BaseComp implements DbDriver {

    private Conf conf;
    private String dbType;
    private NamedList<DbDataType> dbDataTypes = new DefaultNamedList<>();
    private ClassLinks<String> dbDatatypesByClass = new ClassLinks<>();

    //////

    protected void onConfigure(BeanConfig cfg) throws Exception {
        super.onConfigure(cfg);
        //
        this.conf = cfg.getConf();
        //
        for (Conf x : this.conf.getConfs("dbdatatype")) {
            addDbDataType(x);
        }
        //
        for (Conf x : this.conf.getConfs("type")) {
            addType(x);
        }
    }

    protected void addDbDataType(Conf x) {
        DbDataType r = (DbDataType) getApp().create(x);
        dbDataTypes.add(r);
    }

    protected void addType(Conf x) {
        String rn = x.getString("dbdatatype");
        if (UtString.empty(rn)) {
            return; // пропускаем, видимо для чего-то еще нужен
        }
        dbDatatypesByClass.add(x.getName(), rn);
    }

    //////

    public Conf getConf() {
        return conf;
    }

    public String getDbType() {
        return dbType;
    }

    public void setDbType(String dbType) {
        this.dbType = dbType;
    }

    //////

    public NamedList<DbDataType> getDbDataTypes() {
        return dbDataTypes;
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
    protected DbDataType getDbDatatype(Object value) {
        String cn = "null";
        if (value != null) {
            cn = value.getClass().getName();
        }
        String nm = getDbDatatypesByClass().get(cn);
        if (nm == null) {
            throw new XError("Не определен dbdatatype для класса {0}", cn);
        }
        return getDbDataTypes().get(nm);
    }

    public DbDataType getDbDatatype(ResultSetMetaData md, int colIdx) throws Exception {
        String nm = getDbDatatypeName(md, colIdx);
        return getDbDataTypes().get(nm);
    }

    public DbDataType getDbDatatype(int sqlType) throws Exception {
        String nm = getDbDatatypeName(sqlType);
        return getDbDataTypes().get(nm);
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
                DbDataType dbdt = getDbDatatype(value);
                dbdt.setValue(statement, paramIdx, value);
            }
        }
    }

}
