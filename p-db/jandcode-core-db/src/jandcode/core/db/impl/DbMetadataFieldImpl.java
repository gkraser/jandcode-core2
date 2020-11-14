package jandcode.core.db.impl;

import jandcode.commons.named.*;
import jandcode.core.db.*;

public class DbMetadataFieldImpl extends Named implements DbMetadataField {

    private DbDataType dbDataType;
    private int size;

    public DbMetadataFieldImpl(String name, DbDataType dbDataType, int size) {
        setName(name);
        this.dbDataType = dbDataType;
        this.size = size;
    }

    public DbDataType getDbDataType() {
        return dbDataType;
    }

    public int getSize() {
        return size;
    }
}
