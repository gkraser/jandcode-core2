package jandcode.core.dbm.dict.impl;

import jandcode.commons.*;
import jandcode.commons.named.*;
import jandcode.core.dbm.*;
import jandcode.core.dbm.dao.*;
import jandcode.core.dbm.dict.*;
import jandcode.core.store.*;

import java.util.*;

public class StoreDictDataResolver {

    class DictRef implements INamed {
        Dict dict;
        Set<Object> ids = new HashSet<>();

        public DictRef(Dict dict) {
            this.dict = dict;
        }

        public String getName() {
            return dict.getName();
        }

    }

    class DictRefHolder {
        NamedList<DictRef> dicts = new DefaultNamedList<>();

        DictRef getDictRef(Dict dict) {
            DictRef res = dicts.find(dict.getName());
            if (res == null) {
                res = new DictRef(dict);
                dicts.add(res);
            }
            return res;
        }
    }

    class DictRefFld {
        DictRef dictRef;
        int fieldIndex;

        public DictRefFld(DictRef dictRef, int fieldIndex) {
            this.dictRef = dictRef;
            this.fieldIndex = fieldIndex;
        }
    }


    public void resolveDicts(Model model, Store store, List<StoreRecord> records) throws Exception {

        if (records == null) {
            records = store.getRecords();
        }

        DictService dictSvc = model.bean(DictService.class);

        // собираем все словари
        DictRefHolder holder = new DictRefHolder();
        List<DictRefFld> byFields = new ArrayList<>();
        for (StoreField f : store.getFields()) {
            if (UtString.empty(f.getDict())) {
                continue;  // не словарное поле
            }
            Dict dict = dictSvc.getDicts().find(f.getDict());
            if (dict == null) {
                continue; // нет такого словаря
            }
            DictRef dr = holder.getDictRef(dict);
            byFields.add(new DictRefFld(dr, f.getIndex()));
        }

        if (holder.dicts.size() == 0) {
            return; // словарей нет
        }

        // собираем все данные со всех полей
        for (StoreRecord rec : records) {
            for (DictRefFld rf : byFields) {
                if (rec.isValueNull(rf.fieldIndex)) {
                    continue;
                }
                rf.dictRef.ids.add(rec.getValue(rf.fieldIndex));
            }
        }

        // возможно у store уже есть DictDataHolder
        DictDataHolder dictDataHolder = null;
        Object storeDictResolver = store.getDictResolver();
        if (storeDictResolver instanceof DictDataHolder) {
            dictDataHolder = (DictDataHolder) storeDictResolver;
        } else {
            dictDataHolder = new DictDataHolderImpl();
        }

        // фармируем все нужные dictData
        List<DictDao.DictIds> daoParam = new ArrayList<>();
        for (DictRef dictRef : holder.dicts) {
            DictData dd = dictDataHolder.getItems().find(dictRef.getName());
            if (dd == null) {
                dd = dictRef.dict.createDictData();
                dictDataHolder.getItems().add(dd);
            }
            // оставляем только не существующие id
            dictRef.ids = dd.notExistsIds(dictRef.ids);
            if (dictRef.ids.size() > 0) {
                DictDao.DictIds dids = new DictDao.DictIds(dictRef.dict, dictRef.ids);
                daoParam.add(dids);
            }
        }

        //
        if (daoParam.size() == 0) {
            return;  // тут делать нечего
        }

        // загружаем
        DictDao dao = model.bean(ModelDaoService.class).getDaoInvoker().createDao(DictDao.class);
        dao.resolveListIds(daoParam);

        // обновляем данные
        for (DictDao.DictIds dids : daoParam) {
            DictData dd = dictDataHolder.getItems().get(dids.getDict().getName());
            dd.updateData(dids.getDictData().getData());
        }

        // устанавливаем для store
        store.setDictResolver(dictDataHolder);
    }

}
