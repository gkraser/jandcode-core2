package jandcode.core.db.impl;

import jandcode.commons.*;
import jandcode.commons.collect.*;
import jandcode.commons.conf.*;
import jandcode.commons.error.*;
import jandcode.commons.named.*;
import jandcode.commons.variant.*;
import jandcode.core.*;
import jandcode.core.db.*;

import java.sql.*;
import java.util.*;

public class DbDriverImpl extends BaseComp implements DbDriver {

    private Conf conf;
    private String dbType;
    private NamedList<DbDataType> dbDataTypes = new DefaultNamedList<>();
    private ClassLinks<String> dbDatatypesByClass = new ClassLinks<>();
    private String jdbcDriverClassName;

    //////

    protected void onConfigure(BeanConfig cfg) throws Exception {
        super.onConfigure(cfg);
        //
        this.conf = cfg.getConf();
        //
        // формируем раскрытую conf для dbdatatype
        Conf xExp = Conf.create();
        xExp.setValue("dbdatatype", this.conf.getConf("dbdatatype"));

        ConfExpander exp = UtConf.createExpander(xExp);

        Conf confDbDataType = exp.expand("dbdatatype");
        for (Conf x : confDbDataType.getConfs()) {
            addDbDataType(x);
        }
        //
        for (Conf x : this.conf.getConfs("type")) {
            addType(x);
        }
    }

    public String getJdbcDriverClassName() {
        return jdbcDriverClassName;
    }

    public void setJdbcDriverClassName(String jdbcDriverClassName) {
        this.jdbcDriverClassName = jdbcDriverClassName;
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

    protected ClassLinks<String> getDbDataTypesByClass() {
        return dbDatatypesByClass;
    }

    /**
     * Для sql типа возвращает имя dbdatatype
     */
    protected String getDbDataTypeName(int sqltype) {
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
            case Types.ROWID:
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
    protected String getDbDataTypeName(ResultSetMetaData md, int colIdx) throws Exception {
        int ct = md.getColumnType(colIdx);
        String dt = getDbDataTypeName(ct);
        return dt;
    }

    /**
     * dbdatatype для значения
     */
    protected DbDataType getDbDataType(Object value) {
        String cn = "null";
        if (value != null) {
            cn = value.getClass().getName();
        }
        String nm = getDbDataTypesByClass().get(cn);
        if (nm == null) {
            throw new XError("Не определен dbdatatype для класса {0}", cn);
        }
        return getDbDataTypes().get(nm);
    }

    public DbDataType getDbDataType(ResultSetMetaData md, int colIdx) throws Exception {
        String nm = getDbDataTypeName(md, colIdx);
        return getDbDataTypes().get(nm);
    }

    public DbDataType getDbDataType(int sqlType) throws Exception {
        String nm = getDbDataTypeName(sqlType);
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

            if (paramValues.isValueNull(paramName)) {
                setNullParam(statement, paramIdx, dt);
            } else {
                Object value = paramValues.getValue(paramName);
                DbDataType dbdt = getDbDataType(value);
                dbdt.setValue(statement, paramIdx, value);
            }
        }
    }

}
