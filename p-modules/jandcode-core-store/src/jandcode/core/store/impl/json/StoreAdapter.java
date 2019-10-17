package jandcode.core.store.impl.json;

import com.google.gson.*;
import jandcode.core.store.*;

import java.lang.reflect.*;

public class StoreAdapter implements JsonSerializer<Store> {

    public JsonElement serialize(Store src, Type typeOfSrc, JsonSerializationContext context) {
        JsonArray res = new JsonArray();
        for (StoreRecord rec : src.getRecords()) {
            res.add(context.serialize(rec));
        }
        return res;
    }
}
