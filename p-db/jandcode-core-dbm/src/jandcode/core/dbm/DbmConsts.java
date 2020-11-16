package jandcode.core.dbm;

/**
 * Константы для dbm.
 */
public class DbmConsts {

    /**
     * Имя модели, dbsource и т.д. по умолчанию.
     * Вообщем для списковых объектов, среди которых существует некоторый
     * "по умолчанию", он имеет такое имя.
     */
    public static final String DEFAULT = "default";

    /**
     * Имя базового объекта по умолчанию (для домена, поля ...).
     * Вообщем для списковых объектов, среди которых существует некоторый
     * "базовый", он имеет такое имя.
     */
    public static final String BASE = "base";

    /**
     * Модель core, которая всегда включается в любую другую модель первой
     */
    public static final String CORE_MODEL = "jandcode.core.dbm";

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
     * {@link DbmConsts#NULL_STRING_VALUE} или {@link DbmConsts#NULL_STRING_VALUE_XML}.
     */
    public static boolean isNullStringValue(String v) {
        return v == null || v.equals(NULL_STRING_VALUE) || v.equals(NULL_STRING_VALUE_XML);
    }

}
