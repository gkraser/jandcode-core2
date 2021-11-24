package jandcode.core.store.impl.json;

import com.google.gson.*;
import jandcode.commons.*;
import jandcode.core.store.*;

import java.lang.reflect.*;
import java.util.*;

/**
 * Конвертация Store в json.
 * Результат - объект.
 * Поля:
 * <ul>
 *     <li>records - массив записей</li>
 *     <li>dictdata - данные словарей</li>
 *     <li>другие - все customProps, которые являются правильными идентификаторами.
 *     Например свойство 'prop1' будет сконвертировано, а свойство 'no.prop1' - нет</li>
 * </ul>
 */
public class StoreAdapter implements JsonSerializer<Store> {

    public static boolean isIdn(String s) {
        if (UtString.empty(s)) {
            return false;
        }
        int len = s.length();
        if (!UtString.isIdnStartChar(s.charAt(0))) {
            return false;
        }
        for (int i = 1; i < len; ++i) {
            if (!UtString.isIdnChar(s.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    public static JsonObject serializeStore(Store store, List<StoreRecord> records, JsonSerializationContext context) {
        JsonObject res = new JsonObject();

        // records
        JsonArray jsonRecords = new JsonArray();
        if (records != null) {
            for (StoreRecord rec : records) {
                jsonRecords.add(context.serialize(rec.getValues()));
            }
        }
        res.add("records", jsonRecords);

        // dictdata
        IStoreDictResolver dictResolver = store.getDictResolver();
        if (dictResolver != null) {
            Map<String, Object> dictdata = dictResolver.toDictdata();
            if (dictdata != null && dictdata.size() > 0) {
                res.add("dictdata", context.serialize(dictdata));
            }
        }

        // custom props
        Collection<String> propNames = store.getCustomPropNames();
        for (String propName : propNames) {
            if (!isIdn(propName)) {
                continue;
            }
            Object propValue = store.getCustomProp(propName);
            if (propValue == null) {
                continue;
            }
            res.add(propName, context.serialize(propValue));
        }

        return res;
    }

    public JsonElement serialize(Store src, Type typeOfSrc, JsonSerializationContext context) {
        return serializeStore(src, src.getRecords(), context);
    }

}
