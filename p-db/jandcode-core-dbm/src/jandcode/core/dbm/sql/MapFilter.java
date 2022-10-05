package jandcode.core.dbm.sql;

import jandcode.core.dbm.sql.impl.*;

import java.util.*;

/**
 * Map для хранения фильтров.
 * <p>
 * Ключ - имя фильтра.
 * Значение - значение для фильтра. Может быть простым значением (string, long, ...).
 * Может быть map. Можно получить значение как {@link MapFilterValue}.
 * <p>
 * Интерпретацией значений этот класс не занимается, это просто контейнер для хранения
 * данных.
 */
public class MapFilter extends LinkedHashMap<String, Object> {

    public MapFilter() {
        super();
    }

    public MapFilter(Map<? extends String, ?> m) {
        super(m);
    }

    /**
     * Возваращет значение в виде MapFilterValue.
     *
     * @param key ключ
     * @return null, если нет такого ключа, иначе экземпляр MapFilterValue, который
     * проинициализирован значением из ключа
     */
    public MapFilterValue getValue(String key) {
        if (!this.containsKey(key)) {
            return null;
        }
        return new MapFilterValueImpl(key, this.get(key));
    }

}
