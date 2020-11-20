package jandcode.core.dbm.dict.impl;

import jandcode.commons.error.*;
import jandcode.core.dbm.dao.*;
import jandcode.core.dbm.dict.*;

import java.util.concurrent.*;

public class DictCacheImpl implements DictCache {

    private DictServiceImpl dictService;
    protected ConcurrentMap<String, DictData> cache = new ConcurrentHashMap<>();

    public DictCacheImpl(DictServiceImpl dictService) {
        this.dictService = dictService;
    }

    public DictData getDictData(Dict dict) {
        if (!dict.isLoadable()) {
            throw new XError("dict [{0}] не может быть получен из кеша, потому что он не загружаемый", dict.getName());
        }
        DictData res = cache.get(dict.getName());
        if (res == null) {
            // в кеше нет
            try {
                res = loadDict(dict);
                cache.put(dict.getName(), res);
            } catch (Exception e) {
                throw new XErrorMark(e, "загрузка словаря: " + dict.getName());
            }
        }
        return res;
    }

    private DictData loadDict(Dict dict) throws Exception {
        DictDao dao = dictService.getModel().bean(ModelDaoService.class).getDaoInvoker().createDao(DictDao.class);
        DictData dd = dao.loadDict(dict);
        dd.getData().getIndex("id");  // сразу индекс строим
        return dd;
    }

    public void invalidate(Dict dict) {
        cache.remove(dict.getName());
    }

}
