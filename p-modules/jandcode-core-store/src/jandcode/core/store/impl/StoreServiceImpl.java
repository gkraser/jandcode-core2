package jandcode.core.store.impl;

import jandcode.commons.*;
import jandcode.commons.collect.*;
import jandcode.commons.conf.*;
import jandcode.commons.named.*;
import jandcode.commons.reflect.*;
import jandcode.core.*;
import jandcode.core.store.*;

import java.util.*;

public class StoreServiceImpl extends BaseComp implements StoreService {

    static {
        new StaticInit();
    }

    private NamedList<StoreDataType> storeDataTypes = new DefaultNamedList<>("Not found StoreDataType: {0}");
    private ClassLinks<String> storeDatatypesByClass = new ClassLinks<>();
    private NamedList<StoreLoaderDef> storeLoaders = new DefaultNamedList<>("Not found StoreLoader: {0}");
    private NamedList<StoreCalcFieldDef> storeCalcFields = new DefaultNamedList<>("Not found StoreCalcField: {0}");

    class StoreLoaderDef extends Named {
        Conf conf;

        public StoreLoaderDef(String name, Conf conf) {
            setName(name);
            this.conf = conf;
        }

        StoreLoader createInst() {
            return (StoreLoader) getApp().create(this.conf);
        }
    }

    class StoreCalcFieldDef extends Named {
        Conf conf;

        public StoreCalcFieldDef(String name, Conf conf) {
            setName(name);
            this.conf = conf;
        }

        StoreCalcField createInst() {
            return (StoreCalcField) getApp().create(this.conf);
        }
    }

    protected void onConfigure(BeanConfig cfg) throws Exception {
        super.onConfigure(cfg);
        //

        Conf topConf = getApp().getConf().getConf("store");

        for (Conf sf : topConf.getConfs("storedatatype")) {
            StoreDataType sdt = (StoreDataType) getApp().create(sf);
            storeDataTypes.add(sdt);
        }

        //
        for (Conf x : topConf.getConfs("type")) {
            String rn = x.getString("storedatatype");
            if (UtString.empty(rn)) {
                continue; // пропускаем, видимо для чего-то еще нужен
            }
            storeDatatypesByClass.add(x.getName(), rn);
        }

        //
        for (Conf x : topConf.getConfs("storeloader")) {
            storeLoaders.add(new StoreLoaderDef(x.getName(), x));
        }

        //
        for (Conf x : topConf.getConfs("storecalcfield")) {
            storeCalcFields.add(new StoreCalcFieldDef(x.getName(), x));
        }
    }

    public NamedList<StoreDataType> getStoreDataTypes() {
        return storeDataTypes;
    }

    public StoreField createStoreField(String type) {
        StoreDataType sdt = storeDataTypes.get(type);
        return new DefaultStoreField(sdt);
    }

    public StoreField createStoreField(Class valueType) {
        String type = storeDatatypesByClass.get(valueType);
        if (UtString.empty(type)) {
            type = "object";
        }
        return createStoreField(type);
    }

    public Store createStore() {
        return new DefaultStore(getApp(), this);
    }

    public Store createStore(Class cls) {
        ReflectRecord rt = UtReflect.getReflectRecord(cls);
        Store storeStruct = (Store) rt.getProp("storeStruct");
        if (storeStruct == null) {
            synchronized (this) {
                storeStruct = (Store) rt.getProp("storeStruct");
                if (storeStruct == null) {
                    storeStruct = createStore();
                    for (ReflectRecordField rtf : rt.getFields()) {
                        StoreField f = storeStruct.addField(rtf.getName(), rtf.getType());

                        String dict = UtCnv.toString(rtf.getProp("dict"));
                        if (!UtString.empty(dict)) {
                            f.setDict(dict);
                        }

                        int size = UtCnv.toInt(rtf.getProp("size"));
                        if (size > 0) {
                            f.setSize(size);
                        }

                        int scale = UtCnv.toInt(rtf.getProp("scale"));
                        if (scale != StoreField.NO_SCALE) {
                            f.setScale(scale);
                        }

                    }
                    rt.setProp("storeStruct", storeStruct);
                }
            }
        }
        return storeStruct.cloneStore();
    }

    public StoreLoader createStoreLoader(String name) {
        return this.storeLoaders.get(name).createInst();
    }

    public Collection<String> getStoreLoaderNames() {
        return this.storeLoaders.getNames();
    }

    public boolean hasStoreLoader(String name) {
        return this.storeLoaders.find(name) != null;
    }

    public StoreCalcField createStoreCalcField(String name) {
        return this.storeCalcFields.get(name).createInst();
    }

    public Collection<String> getStoreCalcFieldNames() {
        return this.storeCalcFields.getNames();
    }

}
