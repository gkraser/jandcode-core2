package jandcode.core.dbm.dict.impl;

import jandcode.commons.*;
import jandcode.commons.named.*;
import jandcode.core.dbm.*;
import jandcode.core.dbm.dao.*;
import jandcode.core.dbm.dict.*;
import jandcode.core.store.*;

import java.util.*;

public class StoreDictDataResolver {

    static class DictRef implements INamed {
        Dict dict;
        Set<Object> ids = new HashSet<>();

        public DictRef(Dict dict) {
            this.dict = dict;
        }

        public String getName() {
            return dict.getName();
        }

        public void addId(Object id) {
            if (id == null) {
                return;
            }
            if (id instanceof Collection) {
                Collection<?> lst = (Collection<?>) id;
                // коллекция - каждый элемент - id
                for (Object it : lst) {
                    addId(it);
                }
            } else if (id instanceof CharSequence) {
                String s = UtCnv.toString(id);
                if (s.indexOf(',') != -1) {
                    // значения через ','
                    addId(UtCnv.toList(s, ","));
                } else {
                    this.ids.add(id);
                }
            } else {
                this.ids.add(id);
            }
        }
    }

    static class DictRefHolder {
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

    static class DictRefFld {
        DictRef dictRef;
        int fieldIndex;

        public DictRefFld(DictRef dictRef, int fieldIndex) {
            this.dictRef = dictRef;
            this.fieldIndex = fieldIndex;
        }
    }

    static class StoreRef {
        Store store;
        List<StoreRecord> records;

        public StoreRef(Store store, List<StoreRecord> records) {
            this.store = store;
            this.records = records;
            if (this.records == null) {
                this.records = store.getRecords();
            }
        }

    }

    public void resolveDicts(Model model, Store store, List<StoreRecord> records) throws Exception {

        List<StoreRef> storeRefs = new ArrayList<>();
        storeRefs.add(new StoreRef(store, records));

        // возможно у store уже есть DictDataHolder
        DictDataHolder dictDataHolder = null;
        Object storeDictResolver = store.getDictResolver();
        if (storeDictResolver instanceof DictDataHolder) {
            dictDataHolder = (DictDataHolder) storeDictResolver;
        } else {
            dictDataHolder = new DictDataHolderImpl();
        }

        // собираем словари для записей store
        boolean hasDicts = grabDicts(model, storeRefs, dictDataHolder);

        if (hasDicts) {
            // собираем словари у словарей, если у них есть словарные поля
            int level = 0;
            while (level < 5) { // ограничиваем разумным уровнем вложенности словарей со словарями
                storeRefs = new ArrayList<>();
                for (DictData dd : dictDataHolder.getItems()) {
                    storeRefs.add(new StoreRef(dd.getData(), null));
                }
                if (!grabDicts(model, storeRefs, dictDataHolder)) {
                    break;
                }
                level++;
            }
        }

        // устанавливаем для store
        store.setDictResolver(dictDataHolder);
    }

    /**
     * Собрать словари
     *
     * @param model          модель
     * @param storeRefs      набор store и их записей
     * @param dictDataHolder куда собирать
     * @return false - нет словарей
     */
    public boolean grabDicts(Model model, List<StoreRef> storeRefs, DictDataHolder dictDataHolder) throws Exception {

        DictService dictSvc = model.bean(DictService.class);

        DictRefHolder holder = new DictRefHolder();

        // собираем все словари
        for (StoreRef storeRef : storeRefs) {
            List<DictRefFld> byFields = new ArrayList<>();
            for (StoreField f : storeRef.store.getFields()) {
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

            if (byFields.size() == 0) {
                continue; // словарей нет
            }

            // собираем все данные со всех полей
            for (StoreRecord rec : storeRef.records) {
                for (DictRefFld rf : byFields) {
                    if (rec.isValueNull(rf.fieldIndex)) {
                        continue;
                    }
                    rf.dictRef.addId(rec.getValue(rf.fieldIndex));
                }
            }
        }

        // формируем все нужные dictData
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
            return false;  // тут делать нечего
        }

        // загружаем
        DictDao dao = model.bean(ModelDaoService.class).getDaoInvoker().createDao(DictDao.class);
        dao.resolveListIds(daoParam);

        // обновляем данные
        for (DictDao.DictIds dids : daoParam) {
            DictData dd = dictDataHolder.getItems().get(dids.getDict().getName());
            dd.updateData(dids.getDictData().getData());
        }

        return true;
    }

}
