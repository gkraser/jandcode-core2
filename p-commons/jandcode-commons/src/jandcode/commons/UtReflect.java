package jandcode.commons;

import jandcode.commons.reflect.*;
import jandcode.commons.reflect.impl.*;

/**
 * Статические утилиты для рефлексии
 */
public class UtReflect {

    private static ReflectUtils utils = new ReflectUtilsImpl();

    /**
     * Глобальный экземпляр {@link ReflectUtils}.
     */
    public static ReflectUtils getUtils() {
        return utils;
    }

}
