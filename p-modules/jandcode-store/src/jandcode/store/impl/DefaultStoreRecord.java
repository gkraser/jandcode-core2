package jandcode.store.impl;

import jandcode.store.*;

public class DefaultStoreRecord extends BaseStoreRecord {

    protected Object[] data;

    public DefaultStoreRecord(Store store) {
        this.store = store;
        this.data = new Object[store.getCountFields()];
    }

    public boolean isValueNull(int index) {
        if (index >= data.length) {
            return true;
        }
        return data[index] == null;
    }

    public void clear() {
        this.data = new Object[store.getCountFields()];
    }

    public void setRawValue(int index, Object value) {
        if (index < data.length) {
            data[index] = value;
        } else {
            if (index >= getCountFields()) {
                throw new ArrayIndexOutOfBoundsException(index);
            }
            Object[] tmpData = new Object[store.getCountFields()];
            for (int i = 0; i < data.length; i++) {
                tmpData[i] = data[i];
            }
            tmpData[index] = value;
            data = tmpData;
        }
    }

    public Object getRawValue(int index) {
        if (index >= data.length) {
            return null;
        }
        return data[index];
    }

}
