package jandcode.core.dbm.dict.impl;

import jandcode.commons.*;
import jandcode.commons.conf.*;
import jandcode.commons.error.*;
import jandcode.commons.named.*;
import jandcode.core.*;
import jandcode.core.dbm.*;
import jandcode.core.dbm.dao.*;
import jandcode.core.dbm.dict.*;
import jandcode.core.dbm.std.*;
import jandcode.core.store.*;

import java.util.*;

public class DictServiceImpl extends BaseModelMember implements DictService {

    private NamedList<Dict> dicts = new DefaultNamedList<>("Не найден dict [{0}]");
    private DictCache cache = new DictCacheImpl(this);

    protected void onConfigure(BeanConfig cfg) throws Exception {
        super.onConfigure(cfg);
        //

        Conf modelConf = getModel().getConf();
        Conf xExp = UtConf.create();
        xExp.setValue("dict", modelConf.getConf("dict"));

        ConfExpander exp = UtConf.createExpander(xExp);
        exp.addRuleNotInherited("abstract");

        Conf confDict = exp.expand("dict");
        for (Conf x : confDict.getConfs()) {
            if (UtConf.isTagged(x, "abstract")) {
                continue;
            }
            Dict d = null;
            try {
                d = getModel().create(x, DictImpl.class);
            } catch (Exception e) {
                throw new XErrorMark(e, "dict conf: " + x.origin());
            }
            dicts.add(d);
        }

    }

    public NamedList<Dict> getDicts() {
        return dicts;
    }

    public DictData resolveIds(Dict dict, Collection<Object> ids) throws Exception {
        DictDao dao = getModel().bean(ModelDaoService.class).getDaoInvoker().createDao(DictDao.class);
        return dao.resolveIds(dict, ids);
    }

    public void resolveDicts(Object data) throws Exception {
        if (data == null) {
            return;
        }
        if (data instanceof Store) {
            new StoreDictDataResolver().resolveDicts(getModel(), (Store) data, null);

        } else if (data instanceof StoreRecord) {
            StoreRecord rec = (StoreRecord) data;
            new StoreDictDataResolver().resolveDicts(getModel(),
                    rec.getStore(), Collections.singletonList(rec));

        } else if (data instanceof DataBox) {
            DataBox dataBox = (DataBox) data;
            for (Object v : dataBox.values()) {
                resolveDicts(v);
            }

        }
    }

    public DictCache getCache() {
        return cache;
    }

    public DictData loadDictData(Dict dict) {
        DictData cacheDd = getCache().getDictData(dict);
        DictData res = dict.createDictData();
        res.getData().add(cacheDd.getData());
        return res;
    }

    public DictData loadDictData(Dict dict, Collection<Object> ids) throws Exception {
        return resolveIds(dict, ids);
    }
}
