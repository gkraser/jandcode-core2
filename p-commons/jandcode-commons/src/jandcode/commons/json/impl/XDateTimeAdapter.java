package jandcode.commons.json.impl;

import com.google.gson.*;
import jandcode.commons.*;
import jandcode.commons.datetime.*;

import java.lang.reflect.*;

public class XDateTimeAdapter implements JsonSerializer<XDateTime>, JsonDeserializer<XDateTime> {

    public JsonElement serialize(XDateTime src, Type typeOfSrc, JsonSerializationContext context) {
        return new JsonPrimitive(src.toString());
    }

    public XDateTime deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        return UtCnv.toDateTime(json.getAsString());
    }

}
