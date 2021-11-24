package jandcode.core.store.impl.json;

import com.google.gson.*;
import jandcode.core.store.*;

import java.lang.reflect.*;
import java.util.*;

/**
 * Конвертация StoreRecord в json. Запись интрепретируется как Store с одной записью
 * и конвертируется по правилам store.
 *
 * @see StoreAdapter
 */
public class StoreRecordAdapter implements JsonSerializer<StoreRecord> {

    public JsonElement serialize(StoreRecord src, Type typeOfSrc, JsonSerializationContext context) {
        List<StoreRecord> records = new ArrayList<>();
        records.add(src);
        return StoreAdapter.serializeStore(src.getStore(), records, context);
    }

}
