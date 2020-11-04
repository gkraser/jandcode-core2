package jandcode.commons.groovy.impl.json;

import com.google.gson.*;
import groovy.lang.*;

import java.lang.reflect.*;

public class GStringAdapter implements JsonSerializer<GString> {

    public JsonElement serialize(GString src, Type typeOfSrc, JsonSerializationContext context) {
        return new JsonPrimitive(src.toString());
    }
}
