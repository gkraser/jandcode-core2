package jandcode.core.store.impl.json;

import com.google.gson.*;
import jandcode.core.store.*;

import java.lang.reflect.*;

public class StoreRecordAdapter implements JsonSerializer<StoreRecord> {

    public JsonElement serialize(StoreRecord src, Type typeOfSrc, JsonSerializationContext context) {
        return context.serialize(src.getValues());
    }

}
