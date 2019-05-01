package jandcode.db.std.dbdatatype;

import jandcode.commons.named.*;
import jandcode.commons.variant.*;
import jandcode.db.*;

public abstract class BaseDbDataType extends Named implements DbDataType {

    private VariantDataType datatype = VariantDataType.OBJECT;
    private String sqlType = "";

    public VariantDataType getDataType() {
        return datatype;
    }

    public void setDataType(VariantDataType datatype) {
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
