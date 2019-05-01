package jandcode.store.impl;

import jandcode.commons.conf.*;
import jandcode.commons.named.*;
import jandcode.core.*;
import jandcode.store.*;

public class StoreServiceImpl extends BaseComp implements StoreService {

    static {
        new StaticInit();
    }

    private NamedList<StoreDataType> storeDataTypes = new DefaultNamedList<>("Not found StoreDataType: {0}");

    protected void onConfigure(BeanConfig cfg) throws Exception {
        super.onConfigure(cfg);
        //

        Conf dataConf = getApp().getConf().getConf("data");

        for (Conf sf : dataConf.getConfs("storedatatype")) {
            StoreDataType sdt = (StoreDataType) getApp().create(sf);
            storeDataTypes.add(sdt);
        }

    }

    public NamedList<StoreDataType> getStoreDataTypes() {
        return storeDataTypes;
    }

    public StoreField createStoreField(String type) {
        StoreDataType sdt = storeDataTypes.get(type);
        return new DefaultStoreField(sdt);
    }

    public Store createStore() {
        return new DefaultStore(getApp(), this);
    }

}
