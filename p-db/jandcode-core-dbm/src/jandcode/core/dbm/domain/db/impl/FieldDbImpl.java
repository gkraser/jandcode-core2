package jandcode.core.dbm.domain.db.impl;

import jandcode.core.db.*;
import jandcode.core.dbm.domain.*;
import jandcode.core.dbm.domain.db.*;

public class FieldDbImpl extends BaseFieldMember implements FieldDb {

    protected void onConfigureMember() throws Exception {
    }

    public DbDataType getDbDatatype() {
        return getModel().getDbSource().getDbDriver().getDbDataTypes().get(getField().getDbDataType());
    }

    public String getSqlType() {
        return getDbDatatype().getSqlType(getField().getSize());
    }

    public boolean isRefCascade() {
        return getField().getConf().getBoolean("db.refcascade");
    }

}
