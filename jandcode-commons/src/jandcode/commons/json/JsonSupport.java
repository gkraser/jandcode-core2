package jandcode.commons.json;

import jandcode.commons.json.impl.jsonsimple.*;

/**
 * Поддержка json
 */
public class JsonSupport {

    /**
     * Конвертация любого объекта в json
     */
    public String toJson(Object value) {
        return JSONValue.toJSONString(value);
    }

    /**
     * Конвертация json-строки в объект.
     * Тип получаемого значения зависит от json.
     * Map и List обрабатываются корректно.
     */
    public Object fromJson(String s) {
        return JSONValue.parse(s);
    }

}
