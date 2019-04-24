package jandcode.store.impl;

import jandcode.commons.*;
import jandcode.commons.error.*;
import jandcode.core.*;
import jandcode.store.*;

import java.util.*;

public abstract class BaseStore implements Store {

    private App app;
    private String name;
    private IStoreService storeService;

    private List<BaseStoreField> fields = new ArrayList<>();
    private Map<String, StoreField> fieldsByName = new HashMap<>();

    private List<StoreRecord> records = new ArrayList<>();
    private IStoreDictResolver dictResolver;

    public BaseStore(App app, IStoreService storeService) {
        this.app = app;
        this.storeService = storeService;
    }

    ////// abstract

    /**
     * Создать новую пустую запись
     */
    protected abstract StoreRecord createRecord();


    ////// fields

    @SuppressWarnings("unchecked")
    public Iterable<StoreField> getFields() {
        return (List) fields;
    }

    public StoreField findField(String fieldName) {
        return fieldsByName.get(UtStrDedup.lower(fieldName));
    }

    public StoreField getField(String fieldName) {
        StoreField f = findField(fieldName);
        if (f == null) {
            throw new XError("Field not found: {0}", fieldName);
        }
        return f;
    }

    public StoreField getField(int index) {
        return fields.get(index);
    }

    public int getCountFields() {
        return fields.size();
    }

    ////// fields modify

    protected void checkIsEmptyStore() {
        if (this.records.size() > 0) {
            throw new XError("store не пустой");
        }
    }

    protected void addFieldInst(String name, BaseStoreField field) {
        if (field.getIndex() != -1) {
            throw new XError("Экземпляр поля уже использовался");
        }
        if (findField(name) != null) {
            throw new XError("Дублирование имени поля: {0}", name);
        }
        String fnKey = UtStrDedup.lower(name);
        field.setName(name);
        field.setIndex(fields.size());
        fields.add(field);
        fieldsByName.put(fnKey, field);
    }

    public StoreField addField(String name, StoreField field) {
        addFieldInst(name, (BaseStoreField) field);
        return field;
    }

    public StoreField addField(String name, String type, int size) {
        StoreField f = storeService.createStoreField(type);
        f.setSize(size);
        addFieldInst(name, (BaseStoreField) f);
        return f;
    }

    public StoreField addField(String name, String type) {
        return addField(name, type, 0);
    }

    public void removeField(String name) {
        checkIsEmptyStore();
        StoreField f = findField(name);
        if (f == null) {
            return;
        }
        fieldsByName.remove(UtStrDedup.lower(name));
        fields.remove(f);
        for (int i = 0; i < fields.size(); i++) {
            fields.get(i).setIndex(i);
        }
    }

    //////

    public App getApp() {
        return app;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<StoreRecord> getRecords() {
        return records;
    }

    public Iterator<StoreRecord> iterator() {
        return records.iterator();
    }

    public int size() {
        return records.size();
    }

    public StoreRecord get(int index) {
        return records.get(index);
    }

    public void clear() {
        records.clear();
    }

    public void remove(int index) {
        records.remove(index);
    }

    //////

    public StoreRecord add() {
        StoreRecord r = createRecord();
        records.add(r);
        return r;
    }

    public StoreRecord add(Map values) {
        StoreRecord r = createRecord();
        r.setValues(values);
        records.add(r);
        return r;
    }

    public StoreRecord add(StoreRecord rec) {
        StoreRecord r = createRecord();
        r.setValues(rec);
        records.add(r);
        return r;
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

    //////

    public IStoreDictResolver getDictResolver() {
        return dictResolver;
    }

    public void setDictResolver(IStoreDictResolver dictResolver) {
        this.dictResolver = dictResolver;
    }

}
