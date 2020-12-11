package jandcode.core.store.impl;

import jandcode.commons.variant.*;
import jandcode.core.store.*;

import java.util.*;

public class StoreIndexImpl implements StoreIndex {

    private Store store;
    private StoreField field;
    private Map<Object, StoreRecord> index;

    public StoreIndexImpl(Store store, StoreField field) {
        this.store = store;
        this.field = field;
        reindex();
    }

    public Store getStore() {
        return store;
    }

    public StoreField getField() {
        return field;
    }

    private Object prepareKey(Object key) {
        return VariantDataType.toDataType(key, field.getStoreDataType().getDataType());
    }

    public StoreRecord get(Object key) {
        key = prepareKey(key);
        return index.get(key);
    }

    public StoreRecord get(Object key, boolean autoAdd) {
        key = prepareKey(key);
        StoreRecord rec = index.get(key);
        if (rec == null && autoAdd) {
            rec = store.add();
            rec.setValue(field.getIndex(), key);
            index.put(key, rec);
        }
        return rec;
    }

    public void reindex() {
        index = new HashMap<>();
        int fidx = field.getIndex();
        for (StoreRecord record : store) {
            if (record.isValueNull(fidx)) {
                continue;
            }
            index.put(record.getValue(fidx), record);
        }
    }

}
