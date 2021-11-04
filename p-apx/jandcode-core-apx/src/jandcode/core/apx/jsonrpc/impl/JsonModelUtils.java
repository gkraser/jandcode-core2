package jandcode.core.apx.jsonrpc.impl;

import jandcode.core.dbm.dict.*;
import jandcode.core.store.*;

import java.util.*;

/**
 * Утилиты для поддержки конвертации в json объектов модели
 */
public class JsonModelUtils {

    /**
     * Сконвертировать dictdata-данные в map вида:
     * <pre>{@code
     * {
     *      dict: {                   // имя словаря
     *          key: {                // значение id
     *              field:            // имя словарного поля
     *                  value         // значение словарного поля
     *          }
     *      }
     *  }
     * }</pre>
     */
    public static Map dictdata(Store store) {
        Map dictdata = null;
        IStoreDictResolver dr = store.getDictResolver();
        if (dr instanceof DictDataHolder) {
            DictDataHolder ddh = (DictDataHolder) dr;
            dictdata = dictdata(ddh);
            if (dictdata.size() == 0) {
                dictdata = null;
            }
        }
        return dictdata;
    }

    public static Map dictdata(DictDataHolder h) {
        Map<String, Object> res = new LinkedHashMap<>();
        for (DictData dd : h.getItems()) {
            Map m = dictdata(dd);
            res.put(dd.getDict().getName(), m);
        }
        return res;
    }

    public static Map dictdata(DictData dd) {
        List<StoreField> flds = new ArrayList<>();
        for (StoreField f : dd.getData().getFields()) {
            if (f.getName().equals("id")) {
                continue;
            }
            flds.add(f);
        }
        Map res = new LinkedHashMap();
        for (StoreRecord rec : dd.getData()) {
            Object id = rec.get("id");
            Map m = new LinkedHashMap();
            for (StoreField f : flds) {
                m.put(f.getName(), rec.getValue(f.getIndex()));
            }
            res.put(id, m);
        }
        return res;
    }

}
