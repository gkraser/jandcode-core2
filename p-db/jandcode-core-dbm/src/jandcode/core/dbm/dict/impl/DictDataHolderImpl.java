package jandcode.core.dbm.dict.impl;

import jandcode.commons.named.*;
import jandcode.core.dbm.dict.*;
import jandcode.core.store.*;

public class DictDataHolderImpl implements DictDataHolder {

    private NamedList<DictData> items = new DefaultNamedList<>();

    public NamedList<DictData> getItems() {
        return items;
    }

    public Object getDictValue(String dictName, Object key, String dictFieldName) {
        DictData dd = items.find(dictName);
        if (dd == null) {
            return null;
        }
        if (dictFieldName == null) {
            dictFieldName = dd.getDict().getDefaultField();
        }
        StoreField sf = dd.getData().findField(dictFieldName);
        if (sf == null) {
            return null;
        }
        StoreRecord rec = dd.getData().getById(key);
        if (rec == null) {
            return null;
        }
        return rec.getValue(sf.getIndex());
    }

}
