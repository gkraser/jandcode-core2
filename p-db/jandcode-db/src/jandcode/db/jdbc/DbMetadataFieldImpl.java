package jandcode.db.jdbc;

import jandcode.db.*;
import jandcode.commons.named.*;

public class DbMetadataFieldImpl extends Named implements DbMetadataField {

    private DbDatatype dbDatatype;
    private int size;

    public DbMetadataFieldImpl(String name, DbDatatype dbDatatype, int size) {
        setName(name);
        this.dbDatatype = dbDatatype;
        this.size = size;
    }

    public DbDatatype getDbDatatype() {
        return dbDatatype;
    }

    public int getSize() {
        return size;
    }
}
