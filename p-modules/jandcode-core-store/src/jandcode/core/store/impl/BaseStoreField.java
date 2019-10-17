package jandcode.core.store.impl;

import jandcode.commons.error.*;
import jandcode.core.store.*;

/**
 * Предок для реализаций полей
 */
public abstract class BaseStoreField implements StoreField, Cloneable {

    private String name;
    private int index = -1;  // метка, что поле еще не использовалось
    private int size;
    private String dict;
    private StoreDataType storeDataType;

    public StoreDataType getStoreDataType() {
        return storeDataType;
    }

    public void setStoreDataType(StoreDataType storeDataType) {
        this.storeDataType = storeDataType;
    }

    public String getName() {
        return name;
    }

    protected void setName(String name) {
        this.name = name;
    }

    public int getIndex() {
        return index;
    }

    protected void setIndex(int index) {
        this.index = index;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public String getDict() {
        return dict;
    }

    public void setDict(String dict) {
        this.dict = dict;
    }

    protected BaseStoreField cloneField() {
        try {
            return (BaseStoreField) this.clone();
        } catch (Throwable e) {
            throw new XErrorWrap(e);
        }
    }

}
