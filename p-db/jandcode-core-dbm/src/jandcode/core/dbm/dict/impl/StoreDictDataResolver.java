package jandcode.core.dbm.dict.impl;

import jandcode.commons.*;
import jandcode.commons.named.*;
import jandcode.core.store.*;

import java.util.*;

public class StoreDictDataResolver {

    class DictRef extends Named {
        Set<Object> ids = new HashSet<>();

        public DictRef(String name) {
            this.setName(name);
        }
    }

    class DictRefHolder {
        NamedList<DictRef> dicts = new DefaultNamedList<>();

        DictRef getDictRef(String name) {
            DictRef res = dicts.find(name);
            if (res == null) {
                res = new DictRef(name);
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


    public void resolveDictData(Store store) throws Exception {
        // собираем все словари
        DictRefHolder holder = new DictRefHolder();
        List<DictRefFld> byFields = new ArrayList<>();
        for (StoreField f : store.getFields()) {
            if (UtString.empty(f.getDict())) {
                continue;  // не словарное поле
            }
            DictRef dr = holder.getDictRef(f.getDict());
            byFields.add(new DictRefFld(dr, f.getIndex()));
        }

        if (holder.dicts.size() == 0) {
            return; // словарей нет
        }

        // собираем все данные со всех полей
        for (StoreRecord rec : store) {
            for (DictRefFld rf : byFields) {
                if (rec.isValueNull(rf.fieldIndex)) {
                    continue;
                }
                rf.dictRef.ids.add(rec.getValue(rf.fieldIndex));
            }
        }


        //
//        IStoreDictResolver dictResolver = store.getDictResolver();
//        if (!(dictResolver instanceof DictData)) {
//            return;
//        }
//
//        // имеем DictData
//        DictData dictData = (DictData) dictResolver;


    }

}
