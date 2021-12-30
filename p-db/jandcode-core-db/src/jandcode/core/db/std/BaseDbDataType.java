package jandcode.core.db.std;

import jandcode.commons.*;
import jandcode.commons.named.*;
import jandcode.commons.variant.*;
import jandcode.core.db.*;

public abstract class BaseDbDataType extends Named implements DbDataType {

    private VariantDataType datatype = VariantDataType.OBJECT;
    private String sqlType = "";
    private String storeDataTypeName;
    private String sqlValue = "";

    class ValueImpl implements Value {
        Object value;
        boolean isNull;

        public Object getValue() {
            return value;
        }

        public boolean isValueNull() {
            return isNull;
        }
    }

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

    public String getStoreDataTypeName() {
        return storeDataTypeName == null ? getName() : storeDataTypeName;
    }

    public void setStoreDataTypeName(String storeDataTypeName) {
        this.storeDataTypeName = storeDataTypeName;
    }

    //////

    /**
     * Создать null-значение
     */
    protected Value createValueNull() {
        return new ValueImpl();
    }

    /**
     * Создать значение value. Если value==null, помечается как null
     */
    protected Value createValue(Object value) {
        return createValue(value, value == null);
    }

    /**
     * Создать значение value.
     */
    protected Value createValue(Object value, boolean isNull) {
        ValueImpl v = new ValueImpl();
        v.value = value;
        v.isNull = isNull;
        return v;
    }

    //////

    public String getSqlValue() {
        return sqlValue;
    }

    public void setSqlValue(String sqlValue) {
        this.sqlValue = sqlValue;
    }

    public String getSqlValue(Object value) {
        if (value == null) {
            return "null";
        }
        String v = UtCnv.toString(value);
        if (value instanceof Boolean) {
            if ("true".equals(v)) {
                v = "1";
            } else {
                v = "0";
            }
        } else if (v.indexOf("'") != -1) {
            v = v.replace("'", "''");
        }
        return getSqlValue().replace("${value}", v);
    }
}
