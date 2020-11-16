package jandcode.core.db.impl;

import jandcode.commons.named.*;
import jandcode.core.db.*;

public class DbQueryFieldImpl extends Named implements DbQueryField {

    private DbDataType dbDataType;
    private int index;
    private String nameOrig;

    public DbQueryFieldImpl(String name, String nameOrig, int index, DbDataType dbDataType) {
        this.name = name;
        this.nameOrig = nameOrig;
        this.index = index;
        this.dbDataType = dbDataType;
    }

    public int getIndex() {
        return index;
    }

    public DbDataType getDbDataType() {
        return dbDataType;
    }

    public String getNameOrig() {
        return nameOrig;
    }

}
