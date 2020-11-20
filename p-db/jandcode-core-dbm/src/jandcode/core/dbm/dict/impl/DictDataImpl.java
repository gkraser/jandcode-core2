package jandcode.core.dbm.dict.impl;

import jandcode.core.dbm.dict.*;
import jandcode.core.store.*;

import java.util.*;

public class DictDataImpl implements DictData {

    private Dict dict;
    private Store data;

    public DictDataImpl(Dict dict, Store data) {
        this.dict = dict;
        this.data = data;
    }

    public String getName() {
        return dict.getName();
    }

    public Dict getDict() {
        return dict;
    }

    public Store getData() {
        return data;
    }

    public Set<Object> notExistsIds(Set<Object> ids) {
        Set<Object> res = new HashSet<>();
        if (ids == null || ids.size() == 0) {
            return res;
        }
        if (getData().size() == 0) {
            res.addAll(ids);
            return res;
        }
        StoreIndex idx = getData().getIndex("id");
        for (Object id : ids) {
            if (idx.get(id) == null) {
                res.add(id);
            }
        }
        return res;
    }

    public void updateData(Store newData) {
        if (newData.size() == 0) {
            return;
        }
        Store dest = getData();
        if (dest.size() == 0) {
            // нет ничего - просто добавляем
            for (StoreRecord rec : newData) {
                dest.add(rec);
            }
        } else {
            // что то есть
            StoreIndex idx = dest.getIndex("id");
            for (StoreRecord rec : newData) {
                StoreRecord existRec = idx.get(rec.getValue("id"));
                if (existRec != null) {
                    existRec.setValues(rec);
                } else {
                    dest.add(rec);
                }
            }
        }
    }

}
