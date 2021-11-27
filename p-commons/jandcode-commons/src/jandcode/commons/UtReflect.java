package jandcode.commons;

import jandcode.commons.reflect.*;
import jandcode.commons.reflect.impl.*;

/**
 * Статические утилиты для рефлексии
 */
public class UtReflect {

    private static ReflectUtils utils = new ReflectUtilsImpl();
    private static ReflectTableHolderImpl tableHolder = new ReflectTableHolderImpl();

    /**
     * Глобальный экземпляр {@link ReflectUtils}.
     */
    public static ReflectUtils getUtils() {
        return utils;
    }

    /**
     * Получить структуру таблицы по классу.
     *
     * @param cls класс
     * @return структура таблицы
     */
    public static ReflectTable getReflectTable(Class cls) {
        return tableHolder.getItem(cls);
    }

}
