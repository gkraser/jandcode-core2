package jandcode.commons.json;

import com.google.gson.*;
import jandcode.commons.datetime.*;
import jandcode.commons.json.impl.*;

import java.util.*;

/**
 * Поддержка json через gson
 */
public class JsonEngine {

    private List<GsonBuilderIniter> gsonBuilderIniters = new ArrayList<>();

    private Gson gson;
    private Gson gsonPretty;

    public JsonEngine() {
        // стандартные адаптеры
        addGsonBuilderIniter((gsonBuilder) -> {
            gsonBuilder.registerTypeHierarchyAdapter(XDateTime.class, new XDateTimeAdapter());
        });
    }

    /**
     * Добавить инициализазатор GsonBuilder
     */
    public void addGsonBuilderIniter(GsonBuilderIniter initer) {
        this.gson = null;
        this.gsonPretty = null;
        this.gsonBuilderIniters.add(initer);
    }

    protected GsonBuilder createGsonBuilder() {
        GsonBuilder b = new GsonBuilder();
        for (var initer : gsonBuilderIniters) {
            initer.initGsonBuilder(b);
        }
        return b;
    }

    /**
     * Ссылка на Gson
     */
    public Gson getGson() {
        if (gson == null) {
            synchronized (this) {
                if (gson == null) {
                    gson = createGsonBuilder().create();
                }
            }
        }
        return gson;
    }

    /**
     * Ссылка на Gson с оформленными выводом
     */
    public Gson getGsonPretty() {
        if (gsonPretty == null) {
            synchronized (this) {
                if (gsonPretty == null) {
                    gsonPretty = createGsonBuilder().setPrettyPrinting().create();
                }
            }
        }
        return gsonPretty;
    }

    /**
     * Конвертация любого объекта в json
     *
     * @param pretty true - оформленный вывод
     */
    public String toJson(Object value, boolean pretty) {
        if (pretty) {
            return getGsonPretty().toJson(value);
        } else {
            return getGson().toJson(value);
        }
    }

    /**
     * Конвертация json-строки в объект Map, List или примитивный.
     */
    public Object fromJson(String s) {
        return getGson().fromJson(s, Object.class);
    }

    /**
     * Конвертация json-строки в объект указанного типа
     */
    public <T> T fromJson(String s, Class<T> classOfT) {
        return getGson().fromJson(s, classOfT);
    }

}
