package jandcode.commons;

import jandcode.commons.json.*;

/**
 * Простая поддержка json.
 */
public class UtJson {

    private static JsonSupport jsonSupport = new JsonSupport();

    /**
     * Конвертация объекта в json
     */
    public static String toJson(Object value) {
        return jsonSupport.toJson(value);
    }

    /**
     * Конвертация json-строки в объект.
     * Тип получаемого значения зависит от json.
     * Map и List обрабатываются корректно.
     */
    public static Object fromJson(String s) {
        return jsonSupport.fromJson(s);
    }

}
