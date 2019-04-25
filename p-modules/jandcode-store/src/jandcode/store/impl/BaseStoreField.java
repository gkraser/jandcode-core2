package jandcode.store.impl;

import jandcode.commons.error.*;
import jandcode.commons.variant.*;
import jandcode.store.*;

/**
 * Предок для реализаций полей
 */
public abstract class BaseStoreField implements StoreField, Cloneable {

    private String name;
    private int index = -1;  // метка, что поле еще не использовалось
    private int size;
    private String dict;
    private VariantDataType dataType = VariantDataType.OBJECT;

    public VariantDataType getDataType() {
        return dataType;
    }

    protected void setDataType(VariantDataType dataType) {
        this.dataType = dataType;
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
