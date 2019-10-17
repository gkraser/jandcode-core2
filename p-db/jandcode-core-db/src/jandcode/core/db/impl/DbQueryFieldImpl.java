package jandcode.core.db.impl;

import jandcode.commons.named.*;
import jandcode.core.db.*;

public class DbQueryFieldImpl extends Named implements DbQueryField {

    private DbDataType dbDataType;
    private int index;

    public DbQueryFieldImpl(String name, int index, DbDataType dbDataType) {
        this.name = name;
        this.index = index;
        this.dbDataType = dbDataType;
    }

    public int getIndex() {
        return index;
    }

    public DbDataType getDbDataType() {
        return dbDataType;
    }

}
