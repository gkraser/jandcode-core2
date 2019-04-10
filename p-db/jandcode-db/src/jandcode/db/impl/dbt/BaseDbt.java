package jandcode.db.impl.dbt;

import jandcode.core.*;
import jandcode.db.*;
import jandcode.commons.variant.*;

public abstract class BaseDbt extends BaseComp implements DbDatatype {

    private DbSource dbSource;
    private VariantDataType datatype = VariantDataType.OBJECT;
    private String sqlType;

    public DbSource getDbSource() {
        return dbSource;
    }

    public void setDbSource(DbSource dbSource) {
        this.dbSource = dbSource;
    }

    public VariantDataType getDatatype() {
        return datatype;
    }

    public void setDatatype(VariantDataType datatype) {
        this.datatype = datatype;
    }

    public String getSqlType() {
        return sqlType;
    }

    public void setSqlType(String sqlType) {
        this.sqlType = sqlType;
    }

    public String getSqlType(long size) {
        return getSqlType().replace("${size}", "" + size);
    }
}
