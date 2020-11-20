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

    public StoreRecord get(Object key) {
        if (index == null) {
            return null;
        }
        key = VariantDataType.toDataType(key, field.getStoreDataType().getDataType());
        return index.get(key);
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
