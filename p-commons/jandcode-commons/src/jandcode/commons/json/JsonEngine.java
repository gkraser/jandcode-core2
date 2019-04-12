package jandcode.commons.json;

import com.google.gson.*;
import jandcode.commons.datetime.*;
import jandcode.commons.json.impl.*;

/**
 * Поддержка json через gson
 */
public class JsonEngine {

    /**
     * Стандартный gson builder
     */
    private GsonBuilder gsonBuilder = new GsonBuilder();

    private Gson gson;

    public JsonEngine() {
        // стандартные адаптеры
        gsonBuilder.registerTypeHierarchyAdapter(XDateTime.class, new XDateTimeAdapter());
    }

    /**
     * Ссылка на Gson
     */
    public Gson getGson() {
        if (gson == null) {
            synchronized (this) {
                if (gson == null) {
                    gson = gsonBuilder.create();
                }
            }
        }
        return gson;
    }

    /**
     * Возвращает GsonBuilder, с помощью которого создан getGson().
     * При выполнении сбрасывается текущий getGson() и будет создан при следующем
     * обюращении к нему.
     * <p>
     * Используется для глобальной настройки gson.
     */
    public GsonBuilder getGsonBuilder() {
        this.gson = null;
        return gsonBuilder;
    }

    /**
     * Конвертация любого объекта в json
     */
    public String toJson(Object value) {
        return getGson().toJson(value);
    }

    /**
     * Конвертация json-строки в объект Map, List или примитивный.
     */
    public Object fromJson(String s) {
        return getGson().fromJson(s, Object.class);
    }


}
