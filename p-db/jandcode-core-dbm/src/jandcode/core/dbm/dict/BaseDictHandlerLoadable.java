package jandcode.core.dbm.dict;

import jandcode.core.dbm.mdb.*;
import jandcode.core.store.*;

import java.util.*;

/**
 * Базовый предок для обработчиков загружаемых словарей.
 */
public abstract class BaseDictHandlerLoadable implements DictHandler, IDictHandlerLoadDict {

    /**
     * Используем кеш для resolveIds
     */
    public void resolveIds(Mdb mdb, Dict dict, Store data, Collection<Object> ids) throws Exception {
        DictService svc = dict.getModel().bean(DictService.class);
        DictData dd = svc.getCache().getDictData(dict);
        StoreIndex idx = dd.getData().getIndex("id");
        for (Object id : ids) {
            StoreRecord rec = idx.get(id);
            if (rec != null) {
                data.add(rec);
            }
        }
    }

}
