package jandcode.core.dbm.dict.impl;

import jandcode.commons.*;
import jandcode.commons.named.*;
import jandcode.core.dbm.dict.*;
import jandcode.core.store.*;

import java.util.*;

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

    public Map<String, Object> toDictdata() {
        Map<String, Object> res = new LinkedHashMap<>();
        for (DictData dd : getItems()) {
            Map<String, Object> m = toDictdata(dd);
            if (m.size() > 0) {
                res.put(dd.getDict().getName(), m);
            }
        }
        return res;
    }

    protected Map<String, Object> toDictdata(DictData dd) {
        List<StoreField> flds = new ArrayList<>();
        for (StoreField f : dd.getData().getFields()) {
            if (f.getName().equals("id")) {
                continue;
            }
            flds.add(f);
        }
        Map<String, Object> res = new LinkedHashMap<>();
        for (StoreRecord rec : dd.getData()) {
            String id = UtCnv.toString(rec.get("id"));
            Map<String, Object> m = new LinkedHashMap<>();
            for (StoreField f : flds) {
                m.put(f.getName(), rec.getValue(f.getIndex()));
            }
            res.put(id, m);
        }
        return res;
    }

}
