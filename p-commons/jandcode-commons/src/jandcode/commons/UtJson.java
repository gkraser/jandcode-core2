package jandcode.commons;

import com.google.gson.*;
import jandcode.commons.json.*;

/**
 * Поддержка json.
 */
public class UtJson {

    private static JsonEngine jsonEngine = new JsonEngine();

    /**
     * Ссылка на JsonEngine для глобальной настройки json-конверторов.
     */
    public static JsonEngine getJsonEngine() {
        return jsonEngine;
    }

    //////

    /**
     * Конвертация объекта в json
     */
    public static String toJson(Object value) {
        return jsonEngine.toJson(value);
    }

    /**
     * Конвертация json-строки в объект Map, List или примитивный.
     */
    public static Object fromJson(String s) {
        return jsonEngine.fromJson(s);
    }

    //////

    /**
     * Ссылка на Gson
     */
    public static Gson getGson() {
        return jsonEngine.getGson();
    }


}
