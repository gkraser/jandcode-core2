package jandcode.core.dbm.store.impl;

import jandcode.core.dbm.*;
import jandcode.core.dbm.domain.*;
import jandcode.core.dbm.store.*;
import jandcode.core.store.*;

public class ModelStoreServiceImpl extends BaseModelMember implements ModelStoreService {

    public Store createStore() {
        Store store = getApp().bean(StoreService.class).createStore();
        //
        DictData dictData = getModel().create(DictData.class);
        store.setDictResolver(dictData);
        //
        return store;
    }

    public Store createStore(Domain domain) {
        Store store = createStore();
        //
        for (Field f : domain.getFields()) {
            store.addField(f.getName(), f.getStoreDataType());
        }
        //
        return store;
    }
}
