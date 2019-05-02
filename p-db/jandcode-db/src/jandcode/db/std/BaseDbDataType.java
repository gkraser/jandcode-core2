package jandcode.db.std;

import jandcode.commons.named.*;
import jandcode.commons.variant.*;
import jandcode.db.*;

public abstract class BaseDbDataType extends Named implements DbDataType {

    private VariantDataType datatype = VariantDataType.OBJECT;
    private String sqlType = "";
    private String storeDataTypeName;

    class ValueImpl implements Value {
        Object value;
        boolean isNull;

        public Object getValue() {
            return value;
        }

        public boolean isNull() {
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

}
