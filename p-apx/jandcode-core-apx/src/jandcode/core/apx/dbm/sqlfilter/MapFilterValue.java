package jandcode.core.apx.dbm.sqlfilter;

import jandcode.commons.variant.*;

import java.util.*;

/**
 * Значение элемента {@link MapFilter}
 */
public interface MapFilterValue extends IVariant {

    /**
     * Свойства. Имеется ключ 'value'. Остальные - как передано.
     */
    IVariantMap getProps();

    /**
     * Ключ для значения.
     */
    String getKey();

    /**
     * Значение. Берется из props['value']
     */
    Object getValue();

    /**
     * Возвращает true, если параметр пустой.
     * null, пустая строка, пустой Map, пустой List
     */
    boolean isEmpty();

    /**
     * Получить значение как список строк.
     * Если значение - строка, то она рассматривается как набор значений через ','.
     */
    List<String> getValueList();

    /**
     * Получить значение как список уникальных id.
     * Если значение - строка, то она рассматривается как набор значений через ','.
     * Значения 0 в список не попадают.
     */
    Set<Long> getValueIds();

    /**
     * Имя параметра для sql с указанным суффиксом
     */
    String paramName(String suffix);

    /**
     * Имя параметра для sql с суффиксом value
     */
    default String paramName() {
        return paramName(null);
    }

    /**
     * Имя параметра для sql с суффиксом value
     */
    default String getParamName() {
        return paramName(null);
    }

}
