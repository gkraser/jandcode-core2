package jandcode.core.db.impl;

import jandcode.commons.named.*;
import jandcode.core.db.*;

public class DbMetadataTableImpl extends Named implements DbMetadataTable {

    private NamedList<DbMetadataField> fields = new DefaultNamedList<>();
    private boolean view;

    public DbMetadataTableImpl(String name, boolean view) {
        setName(name);
        this.view = view;
    }

    public NamedList<DbMetadataField> getFields() {
        return fields;
    }

    public DbMetadataField addField(String name, DbDataType datatype, int size) {
        DbMetadataFieldImpl f = new DbMetadataFieldImpl(name, datatype, size);
        fields.add(f);
        return f;
    }

    public boolean isView() {
        return view;
    }

}
