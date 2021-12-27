package jandcode.core.dbm.ddl.impl;

import jandcode.commons.simxml.*;
import jandcode.commons.variant.*;
import jandcode.core.dbm.*;
import jandcode.core.dbm.ddl.*;

public abstract class BaseDDLOper extends BaseModelMember implements DDLOper {

    public void saveToXml(SimXml x) throws Exception {
        IVariantMap attrs = x.getAttrs();
        attrs.put("name", getName());
        attrs.put("type", getType());
        onSaveToXml(x);
    }

    protected abstract void onSaveToXml(SimXml x) throws Exception;

    public void loadFromXml(SimXml x) throws Exception {
        IVariantMap attrs = x.getAttrs();
        setName(attrs.getString("name"));
        onLoadFromXml(x);
    }

    protected abstract void onLoadFromXml(SimXml x) throws Exception;

}
