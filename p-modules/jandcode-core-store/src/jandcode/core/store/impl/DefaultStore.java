package jandcode.core.store.impl;

import jandcode.core.*;
import jandcode.core.store.*;

public class DefaultStore extends BaseStore {

    public DefaultStore(App app, IStoreService storeService) {
        super(app, storeService);
    }


    /**
     * Создать новую пустую запись
     */
    protected StoreRecord createRecord() {
        return new DefaultStoreRecord(this);
    }

}
