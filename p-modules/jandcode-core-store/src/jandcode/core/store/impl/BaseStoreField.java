package jandcode.core.store.impl;

import jandcode.commons.*;
import jandcode.commons.error.*;
import jandcode.core.store.*;
import jandcode.core.store.std.*;

/**
 * Предок для реализаций полей
 */
public abstract class BaseStoreField implements StoreField, Cloneable {

    private String name;
    private int index = -1;  // метка, что поле еще не использовалось
    private int size;
    private String dict;
    private StoreDataType storeDataType;
    private int scale = NO_SCALE;
    private String title;
    private StoreCalcField calc;

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

    public StoreField size(int size) {
        setSize(size);
        return this;
    }

    public String getDict() {
        return dict;
    }

    public void setDict(String dict) {
        this.dict = dict;
    }

    public StoreField dict(String dict) {
        setDict(dict);
        return this;
    }

    public int getScale() {
        return scale;
    }

    public void setScale(int scale) {
        this.scale = scale;
    }

    public StoreField scale(int scale) {
        setScale(scale);
        return this;
    }

    public String getTitle() {
        if (UtString.empty(this.title)) {
            return getName();
        }
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public StoreField title(String title) {
        setTitle(title);
        return this;
    }

    protected BaseStoreField cloneField() {
        try {
            return (BaseStoreField) this.clone();
        } catch (Throwable e) {
            throw new XErrorWrap(e);
        }
    }

    public StoreCalcField getCalc() {
        return calc;
    }

    public void setCalc(StoreCalcField calc) {
        if (this.calc != null && this.storeDataType instanceof StoreDataType_calc) {
            // calc уже был установлен, возвращаем тот datatype, который был ранее
            this.storeDataType = ((StoreDataType_calc) this.storeDataType).getBaseStoreDataType();
        }
        this.calc = calc;
        this.storeDataType = new StoreDataType_calc(this.storeDataType, this.calc);
    }

    public StoreField calc(StoreCalcField calc) {
        setCalc(calc);
        return this;
    }

}
