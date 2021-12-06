package jandcode.core.store.impl;

import jandcode.commons.*;
import jandcode.commons.collect.*;
import jandcode.commons.conf.*;
import jandcode.commons.named.*;
import jandcode.commons.reflect.*;
import jandcode.core.*;
import jandcode.core.store.*;

public class StoreServiceImpl extends BaseComp implements StoreService {

    static {
        new StaticInit();
    }

    private NamedList<StoreDataType> storeDataTypes = new DefaultNamedList<>("Not found StoreDataType: {0}");
    private ClassLinks<String> storeDatatypesByClass = new ClassLinks<>();

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

                    }
                    rt.setProp("storeStruct", storeStruct);
                }
            }
        }
        return storeStruct.cloneStore();
    }

}
