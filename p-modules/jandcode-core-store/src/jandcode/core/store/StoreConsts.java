package jandcode.core.store;

/**
 * Константы для store.
 */
public class StoreConsts {

    /**
     * Строка, которая рассматривается как null-значение в некоторых специфических
     * случаях.
     */
    public static final String NULL_STRING_VALUE = "<null>";

    /**
     * Строка, которая рассматривается как null-значение в некоторых специфических
     * случаях при работе с xml.
     */
    public static final String NULL_STRING_VALUE_XML = "{null}";

    /**
     * Возвращает true, если строка имеет значение "null".
     * Для этого она должна быть null или иметь значение
     * {@link StoreConsts#NULL_STRING_VALUE} или {@link StoreConsts#NULL_STRING_VALUE_XML}.
     */
    public static boolean isNullStringValue(String v) {
        return v == null || v.equals(NULL_STRING_VALUE) || v.equals(NULL_STRING_VALUE_XML);
    }

}
