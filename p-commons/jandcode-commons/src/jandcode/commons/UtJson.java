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
        return jsonEngine.toJson(value, false);
    }

    /**
     * Конвертация объекта в json
     *
     * @param pretty true - офрмленный json
     */
    public static String toJson(Object value, boolean pretty) {
        return jsonEngine.toJson(value, pretty);
    }

    /**
     * Конвертация json-строки в объект Map, List или примитивный.
     */
    public static Object fromJson(String s) {
        return jsonEngine.fromJson(s);
    }

    /**
     * Конвертация json-строки в объект указанного типа
     */
    public static <T> T fromJson(String s, Class<T> classOfT) {
        return jsonEngine.fromJson(s, classOfT);
    }

    //////

    /**
     * Ссылка на Gson
     */
    public static Gson getGson() {
        return jsonEngine.getGson();
    }

}
