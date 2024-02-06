package jandcode.core.store;

import java.util.*;

/**
 * Интерфейс для получения значений словарных полей.
 */
public interface IStoreDictResolver {

    /**
     * Возврашает значение для словаря
     *
     * @param dictName      для какого словаря
     * @param key           ключ в словаре
     * @param dictFieldName поле в словаре. Если не указано, то расчитываем на
     *                      поле по умолчанию.
     * @return значение поля словаря или null, если не найдено
     */
    Object getDictValue(String dictName, Object key, String dictFieldName);

    /**
     * Получить данные словарей в виде Map формата dictdata:
     * <pre>{@code
     * {
     *      dict: {                   // имя словаря
     *          key: {                // значение id
     *              field:            // имя словарного поля
     *                  value         // значение словарного поля
     *          }
     *      }
     *  }
     * }</pre>
     */
    default Map<String, Object> toDictdata() {
        return new HashMap<>();
    }

    /**
     * Возвращает true, если словарь допускает в качестве значения словарного поля строку, разделенную ','
     *
     * @param dictName имя словаря
     */
    default boolean isDictMultiValue(String dictName) {
        return true;
    }

}
