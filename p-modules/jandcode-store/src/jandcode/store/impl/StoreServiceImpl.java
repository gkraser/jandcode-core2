package jandcode.store.impl;

import jandcode.commons.*;
import jandcode.commons.conf.*;
import jandcode.commons.error.*;
import jandcode.core.*;
import jandcode.store.*;

import java.util.*;

public class StoreServiceImpl extends BaseComp implements StoreService {

    static {
        new StaticInit();
    }

    private Map<String, Class> fieldTypes = new LinkedHashMap<>();

    protected void onConfigure(BeanConfig cfg) throws Exception {
        super.onConfigure(cfg);
        //

        Conf dataConf = getApp().getConf().getConf("data");

        for (Conf sf : dataConf.getConfs("store-field")) {
            Class cls = UtClass.getClass(sf.getString("class"));
            fieldTypes.put(sf.getName(), cls);
        }

    }

    public Collection<String> getFieldTypes() {
        return fieldTypes.keySet();
    }

    public StoreField createStoreField(String type) {
        Class cls = fieldTypes.get(type);
        if (cls == null) {
            throw new XError("Тип поля {0} не найден");
        }
        return (StoreField) getApp().create(cls);
    }

    public Store createStore() {
        return new DefaultStore(getApp(), this);
    }

}
