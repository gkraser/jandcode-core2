package jandcode.core.store.std;

import jandcode.core.*;
import jandcode.core.store.*;

import java.io.*;

/**
 * Базовый предок для загрузчиков store
 */
public abstract class BaseStoreLoader extends BaseComp implements StoreLoader {

    /**
     * Реализация загрузки
     *
     * @param reader       откуда
     * @param store        куда
     * @param createStruct true - нужно создать структуру store в процессе загрузки
     */
    public abstract void doLoad(Reader reader, Store store, boolean createStruct) throws Exception;

    //////

    private Store store;

    public void setStore(Store store) {
        this.store = store;
    }

    public Store getStore() {
        return store;
    }

    public void loadFrom(Reader reader) throws Exception {
        boolean createStruct = false;
        Store store = getStore();
        if (store == null) {
            createStruct = true;
            store = getApp().bean(StoreService.class).createStore();
        }
        doLoad(reader, store, createStruct);
        if (createStruct) {
            setStore(store);
        }
    }

}
