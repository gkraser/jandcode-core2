package jandcode.core.store.std;

import jandcode.core.*;
import jandcode.core.store.*;

import java.util.*;

/**
 * Обертка для Store
 */
public class BaseStoreWrapper implements Store {

    private Store store;

    public BaseStoreWrapper(Store store) {
        this.store = store;
    }

    public Store getStore() {
        return store;
    }

    //////

    public String getName() {
        return getStore().getName();
    }

    public void setName(String name) {
        getStore().setName(name);
    }

    public StoreField addField(String name, StoreField field) {
        return getStore().addField(name, field);
    }

    public StoreField addField(String name, String type) {
        return getStore().addField(name, type);
    }

    public StoreField addField(String name, String type, int size) {
        return getStore().addField(name, type, size);
    }

    public StoreField addField(String name, Class valueType) {
        return getStore().addField(name, valueType);
    }

    public StoreField addField(String name, Class valueType, int size) {
        return getStore().addField(name, valueType, size);
    }

    public void removeField(String name) {
        getStore().removeField(name);
    }

    public int size() {
        return getStore().size();
    }

    public StoreRecord get(int index) {
        return getStore().get(index);
    }

    public List<StoreRecord> getRecords() {
        return getStore().getRecords();
    }

    public StoreRecord add() {
        return getStore().add();
    }

    public StoreRecord add(Map values) {
        return getStore().add(values);
    }

    public StoreRecord add(StoreRecord rec) {
        return getStore().add(rec);
    }

    public StoreRecord add(Object inst) {
        return getStore().add(inst);
    }

    public void add(Store store) {
        getStore().add(store);
    }

    public void clear() {
        getStore().clear();
    }

    public void remove(int index) {
        getStore().remove(index);
    }

    public IStoreDictResolver getDictResolver() {
        return getStore().getDictResolver();
    }

    public void setDictResolver(IStoreDictResolver dictResolver) {
        getStore().setDictResolver(dictResolver);
    }

    public Store cloneStore() {
        return getStore().cloneStore();
    }

    public void copyTo(Store storeDest) {
        getStore().copyTo(storeDest);
    }

    public StoreRecord getBy(String fieldName, Object key) {
        return getStore().getBy(fieldName, key);
    }

    public StoreRecord getById(Object key) {
        return getStore().getById(key);
    }

    public StoreIndex getIndex(String fieldName) {
        return getStore().getIndex(fieldName);
    }

    public void clearIndex() {
        getStore().clearIndex();
    }

    public void sort(String fields) {
        getStore().sort(fields);
    }

    public Iterator<StoreRecord> iterator() {
        return getStore().iterator();
    }

    public App getApp() {
        return getStore().getApp();
    }

    public void setCustomProp(String name, Object value) {
        getStore().setCustomProp(name, value);
    }

    public Object getCustomProp(String name) {
        return getStore().getCustomProp(name);
    }

    public Collection<String> getCustomPropNames() {
        return getStore().getCustomPropNames();
    }

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

    public boolean hasField(String fieldName) {
        return getStore().hasField(fieldName);
    }

    public Collection<String> getFieldNames() {
        return getStore().getFieldNames();
    }

}
