package jandcode.db.jdbc;

import jandcode.db.*;
import jandcode.commons.named.*;

public class DbMetadataTableImpl extends Named implements DbMetadataTable {

    private NamedList<DbMetadataField> fields = new DefaultNamedList<>();

    public DbMetadataTableImpl(String name) {
        setName(name);
    }

    public NamedList<DbMetadataField> getFields() {
        return fields;
    }

    public DbMetadataField addField(String name, DbDatatype datatype, int size) {
        DbMetadataFieldImpl f = new DbMetadataFieldImpl(name, datatype, size);
        fields.add(f);
        return f;
    }

}
