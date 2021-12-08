package jandcode.core.store.impl;

import jandcode.commons.*;
import jandcode.commons.error.*;
import jandcode.commons.variant.*;
import jandcode.core.*;
import jandcode.core.store.*;

import java.util.*;

public abstract class BaseStore implements Store, Cloneable {

    private App app;
    private String name;
    private IStoreService storeService;

    private List<BaseStoreField> fields = new ArrayList<>();
    private Map<String, StoreField> fieldsByName = new HashMap<>();

    private List<StoreRecord> records = new ArrayList<>();
    private IStoreDictResolver dictResolver;
    private ContainerFieldsMapper fieldsMapper;

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
        StoreField f = fieldsByName.get(UtStrDedup.lower(fieldName));
        if (f == null && fieldsMapper != null) {
            String newFieldName = fieldsMapper.mapField(fieldName, this);
            if (newFieldName != null) {
                return fieldsByName.get(UtStrDedup.lower(newFieldName));
            }
        }
        return f;
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

    public boolean hasField(String fieldName) {
        return fieldsByName.get(UtStrDedup.lower(fieldName)) != null;
    }

    public Collection<String> getFieldNames() {
        List<String> res = new ArrayList<>();
        for (var f : this.fields) {
            res.add(f.getName());
        }
        return res;
    }

    public Store withFieldsMapper(IVariantFieldsMapper fieldsMapper) {
        if (fieldsMapper == null) {
            return this;
        }
        if (this.fieldsMapper == null) {
            this.fieldsMapper = new ContainerFieldsMapper();
        }
        this.fieldsMapper.add(fieldsMapper);
        return this;
    }

    public Store withFieldsMapper(Map<String, String> fields) {
        return withFieldsMapper(new MapFieldsMapper(fields));
    }

    public Iterable<IVariantFieldsMapper> getFieldsMappers() {
        if (this.fieldsMapper == null) {
            return List.of();
        } else {
            return this.fieldsMapper.getFieldsMappers();
        }
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

    public StoreField addField(String name, Class valueType) {
        return addField(name, valueType, 0);
    }

    public StoreField addField(String name, Class valueType, int size) {
        StoreField f = storeService.createStoreField(valueType);
        f.setSize(size);
        addFieldInst(name, (BaseStoreField) f);
        return f;
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
        this.index = null;
        records.clear();
    }

    public void remove(int index) {
        this.index = null;
        records.remove(index);
    }

    //////

    public StoreRecord add() {
        this.index = null;
        StoreRecord r = createRecord();
        records.add(r);
        return r;
    }

    public StoreRecord add(Map values) {
        this.index = null;
        StoreRecord r = createRecord();
        r.setValues(values);
        records.add(r);
        return r;
    }

    public StoreRecord add(StoreRecord rec) {
        this.index = null;
        StoreRecord r = createRecord();
        r.setValues(rec);
        records.add(r);
        return r;
    }

    public StoreRecord add(Object inst) {
        this.index = null;
        StoreRecord r = createRecord();
        r.setValues(inst);
        records.add(r);
        return r;
    }

    public void add(Store store) {
        this.index = null;
        store.copyTo(this);
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
                customProps = new LinkedHashMap<>();
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

    public Collection<String> getCustomPropNames() {
        if (customProps == null) {
            return new ArrayList<>();
        }
        return customProps.keySet();
    }

    //////

    public IStoreDictResolver getDictResolver() {
        return dictResolver;
    }

    public void setDictResolver(IStoreDictResolver dictResolver) {
        this.dictResolver = dictResolver;
    }

    //////

    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    public Store cloneStore() {
        try {
            BaseStore res = (BaseStore) this.clone();
            res.fields = new ArrayList<>();
            res.fieldsByName = new HashMap<>();
            res.records = new ArrayList<>();
            res.customProps = null;

            for (BaseStoreField f : this.fields) {
                BaseStoreField fNew = f.cloneField();
                fNew.setIndex(-1);
                res.addFieldInst(f.getName(), fNew);
            }

            return res;
        } catch (Throwable e) {
            throw new XErrorWrap(e);
        }
    }

    ////// copy

    public void copyTo(Store storeDest) {
        StoreCopier cp = new StoreCopier(this, storeDest);
        cp.copyStore(this, storeDest);
    }

    ////// index

    private Map<String, StoreIndex> index;

    public void clearIndex() {
        this.index = null;
    }

    public StoreIndex getIndex(String fieldName) {
        StoreField f = getField(fieldName);
        if (index == null) {
            index = new HashMap<>();
        }
        StoreIndex idx = index.get(f.getName());
        if (idx == null) {
            idx = new StoreIndexImpl(this, f);
            index.put(f.getName(), idx);
        }
        return idx;
    }

    public StoreRecord getBy(String fieldName, Object key) {
        return getIndex(fieldName).get(key);
    }

    public StoreRecord getById(Object key) {
        return getBy("id", key);
    }

    ////// sort

    public void sort(String fields) {
        if (UtString.empty(fields)) {
            return;
        }
        List<String> fieldsList = UtCnv.toList(fields);

        class SortInfo {
            VariantDataType dataType;
            StoreField field;
            int fieldIndex;
            boolean desc;
        }

        List<SortInfo> sortInfos = new ArrayList<>();
        for (String fn : fieldsList) {
            SortInfo si = new SortInfo();
            String f = UtString.removePrefix(fn, "*");
            if (f == null) {
                si.desc = false;
                si.field = getField(fn);
            } else {
                si.desc = true;
                si.field = getField(f);
            }
            si.fieldIndex = si.field.getIndex();
            si.dataType = si.field.getStoreDataType().getDataType();
            sortInfos.add(si);
        }

        if (sortInfos.size() == 1) {
            SortInfo si = sortInfos.get(0);
            getRecords().sort((o1, o2) -> {
                int v = VariantDataType.compare(o1.getValue(si.fieldIndex),
                        o2.getValue(si.fieldIndex), si.dataType);
                if (si.desc) {
                    v = -v;
                }
                return v;
            });
        } else {
            getRecords().sort((o1, o2) -> {
                int v = 0;
                for (SortInfo si : sortInfos) {
                    v = VariantDataType.compare(o1.getValue(si.fieldIndex),
                            o2.getValue(si.fieldIndex), si.dataType);
                    if (v != 0) {
                        if (si.desc) {
                            v = -v;
                        }
                        break;
                    }
                }
                return v;
            });
        }
    }

    ////// values

    public Set<Object> getUniqueValues(String fieldName) {
        Set<Object> res = new LinkedHashSet<>();
        int fidx = getField(fieldName).getIndex();
        for (StoreRecord record : this) {
            if (record.isValueNull(fidx)) {
                continue;
            }
            res.add(record.getValue(fidx));
        }
        return res;
    }

}
