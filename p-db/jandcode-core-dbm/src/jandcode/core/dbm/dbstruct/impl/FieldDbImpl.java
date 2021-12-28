package jandcode.core.dbm.dbstruct.impl;

import jandcode.core.db.*;
import jandcode.core.dbm.dbstruct.*;
import jandcode.core.dbm.domain.*;

public class FieldDbImpl extends BaseFieldMember implements FieldDb {

    private boolean refCascade;

    protected void onConfigureMember() throws Exception {
        this.refCascade = getField().getConf().getBoolean("db.refcascade");
    }

    public DbDataType getDbDatatype() {
        return getModel().getDbSource().getDbDriver().getDbDataTypes().get(getField().getDbDataType());
    }

    public String getSqlType() {
        return getDbDatatype().getSqlType(getField().getSize());
    }

    public boolean isRefCascade() {
        return refCascade;
    }

}
