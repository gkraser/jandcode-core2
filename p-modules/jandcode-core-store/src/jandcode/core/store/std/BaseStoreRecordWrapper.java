package jandcode.core.store.std;

import jandcode.core.store.*;

import java.util.*;

/**
 * Обертка для StoreRecord
 */
public class BaseStoreRecordWrapper implements StoreRecord {

    private StoreRecord storeRecord;

    public BaseStoreRecordWrapper(StoreRecord storeRecord) {
        this.storeRecord = storeRecord;
    }

    public StoreRecord getStoreRecord() {
        return storeRecord;
    }

    //////

    public Store getStore() {
        return getStoreRecord().getStore();
    }

    public boolean isValueNull(int index) {
        return getStoreRecord().isValueNull(index);
    }

    public Map<String, Object> getValues() {
        return getStoreRecord().getValues();
    }

    public void setValues(Map values) {
        getStoreRecord().setValues(values);
    }

    public void setValues(StoreRecord rec) {
        getStoreRecord().setValues(rec);
    }

    public void setValues(Object inst) {
        getStoreRecord().setValues(inst);
    }

    public Object get(String fieldName) {
        return getStoreRecord().get(fieldName);
    }

    public void set(String fieldName, Object value) {
        getStoreRecord().set(fieldName, value);
    }

    public void clear() {
        getStoreRecord().clear();
    }

    public String getDictText(String fieldName, String dictField) {
        return getStoreRecord().getDictText(fieldName, dictField);
    }

    public String getDictText(String fieldName) {
        return getStoreRecord().getDictText(fieldName);
    }

    public Object getDictValue(String fieldName, String dictField) {
        return getStoreRecord().getDictValue(fieldName, dictField);
    }

    public Object getDictValue(String fieldName) {
        return getStoreRecord().getDictValue(fieldName);
    }

    //////

    public Object getValue(int index) {
        return getStoreRecord().getValue(index);
    }

    public void setValue(int index, Object value) {
        getStoreRecord().setValue(index, value);
    }

    public Object getValue(String name) {
        return getStoreRecord().getValue(name);
    }

    public void setValue(String name, Object value) {
        getStoreRecord().setValue(name, value);
    }

    //////

    public Iterable<StoreField> getFields() {
        return getStoreRecord().getFields();
    }

    public StoreField findField(String fieldName) {
        return getStoreRecord().findField(fieldName);
    }

    public StoreField getField(String fieldName) {
        return getStoreRecord().getField(fieldName);
    }

    public StoreField getField(int index) {
        return getStoreRecord().getField(index);
    }

    public int getCountFields() {
        return getStoreRecord().getCountFields();
    }

    public boolean hasField(String fieldName) {
        return getStoreRecord().hasField(fieldName);
    }

    public Collection<String> getFieldNames() {
        return getStoreRecord().getFieldNames();
    }

}
