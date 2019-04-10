package jandcode.db.impl;

import jandcode.db.*;
import jandcode.commons.named.*;
import jandcode.commons.variant.*;

public class DbQueryFieldImpl extends Named implements DbQueryField {

    private DbDatatype dbDatatype;
    private int index;

    public DbQueryFieldImpl(String name, int index, DbDatatype dbDatatype) {
        this.name = name;
        this.index = index;
        this.dbDatatype = dbDatatype;
    }

    public VariantDataType getDatatype() {
        return getDbDatatype().getDatatype();
    }

    public int getIndex() {
        return index;
    }

    public DbDatatype getDbDatatype() {
        return dbDatatype;
    }

    public String getDbDatatypeName() {
        return dbDatatype.getName();
    }

}
