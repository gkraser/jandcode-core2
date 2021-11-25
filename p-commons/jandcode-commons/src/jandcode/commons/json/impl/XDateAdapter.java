package jandcode.commons.json.impl;

import com.google.gson.*;
import jandcode.commons.*;
import jandcode.commons.datetime.*;

import java.lang.reflect.*;

public class XDateAdapter implements JsonSerializer<XDate>, JsonDeserializer<XDate> {

    public JsonElement serialize(XDate src, Type typeOfSrc, JsonSerializationContext context) {
        return new JsonPrimitive(src.toString());
    }

    public XDate deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        return UtCnv.toDate(json.getAsString());
    }

}
