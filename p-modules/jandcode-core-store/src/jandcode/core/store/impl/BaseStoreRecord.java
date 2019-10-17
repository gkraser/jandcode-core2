package jandcode.core.store.impl;

import jandcode.commons.*;
import jandcode.commons.variant.*;
import jandcode.core.store.*;

import java.util.*;

/**
 * Базовая реализация record. Для наследования.
 */
public abstract class BaseStoreRecord implements StoreRecord, IRawRecord {

    /**
     * Ссылка на store
     */
    protected Store store;

    public Store getStore() {
        return store;
    }

    ////// fields delegate

    public Iterable<StoreField> getFields() {
        return getStore().getFields();
    }

    public StoreField findField(String fieldName) {
        return getStore().findField(fieldName);
    }

    public StoreField getField(String fieldName) {
        return getStore().getField(fieldName);
    }

    public StoreField getField(int index) {
        return getStore().getField(index);
    }

    public int getCountFields() {
        return getStore().getCountFields();
    }

    //////

    public Map<String, Object> getValues() {
        Map<String, Object> m = new LinkedHashMap<>();
        for (StoreField field : getFields()) {
            if (isValueNull(field.getIndex())) {
                continue;
            }
            m.put(field.getName(), getValue(field.getIndex()));
        }
        return m;
    }

    public void setValues(Map values) {
        if (values == null) {
            return;
        }
        for (Object key : values.keySet()) {
            StoreField f = findField(UtString.toString(key));
            if (f == null) {
                continue;
            }
            setValue(f.getIndex(), values.get(key));
        }
    }

    public void setValues(StoreRecord rec) {
        if (rec == null) {
            return;
        }
        for (StoreField fsrc : rec.getFields()) {
            StoreField f = findField(fsrc.getName());
            if (f == null) {
                continue;
            }
            setValue(f.getIndex(), rec.getValue(fsrc.getIndex()));
        }
    }

    public Object get(String fieldName) {
        return getValue(fieldName);
    }

    public void set(String fieldName, Object value) {
        setValue(fieldName, value);
    }

    public void setValue(String name, Object value) {
        StoreField f = getField(name);
        setValue(f.getIndex(), value);
    }

    public Object getValue(String name) {
        StoreField f = getField(name);
        return getValue(f.getIndex());
    }

    public Object getValue(int index) {
        StoreField f = getStore().getField(index);
        return f.getStoreDataType().getFieldValue(this, f.getIndex(), this, f);
    }

    public boolean isNull(String name) {
        StoreField f = getStore().getField(name);
        return isValueNull(f.getIndex());
    }

    public void setValue(int index, Object value) {
        StoreField f = getStore().getField(index);
        f.getStoreDataType().setFieldValue(this, f.getIndex(), value, this, f);
    }

    ////// datatype

    public VariantDataType getDataType(String name) {
        return getField(name).getStoreDataType().getDataType();
    }

    ////// ICustomProp

    private Map<String, Object> customProps;

    public void setCustomProp(String name, Object value) {
        if (value == null) {
            if (customProps != null) {
                customProps.remove(name);
            }
        } else {
            if (customProps == null) {
                customProps = new HashMap<>();
            }
            customProps.put(name, value);
        }
    }

    public Object getCustomProp(String name) {
        if (customProps == null) {
            return null;
        }
        return customProps.get(name);
    }

    ////// dicts

    public String getDictText(String fieldName, String dictField) {
        return UtCnv.toString(getDictValue(fieldName, dictField));
    }

    public String getDictText(String fieldName) {
        return UtCnv.toString(getDictValue(fieldName));
    }

    public Object getDictValue(String fieldName) {
        return getDictValue(fieldName, null);
    }

    public Object getDictValue(String fieldName, String dictField) {
        IStoreDictResolver dr = getStore().getDictResolver();
        if (dr == null) {
            return null;
        }
        StoreField f = findField(fieldName);
        if (f == null) {
            return null;
        }
        if (UtString.empty(f.getDict())) {
            return null;
        }
        return dr.getDictValue(f.getDict(), getValue(f.getIndex()), dictField);
    }

}
