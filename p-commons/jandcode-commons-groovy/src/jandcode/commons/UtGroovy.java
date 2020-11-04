package jandcode.commons;

import jandcode.commons.groovy.*;
import jandcode.commons.groovy.impl.*;

/**
 * Утилиты для groovy
 */
public class UtGroovy {

    static {
        new StaticInit();
    }

    /**
     * Создает компилятор groovy и регистрирует его.
     * Для освобождения ресурсов компилятора,
     * вызовите {@link UtGroovy#destroyCompiler(GroovyCompiler)}.
     */
    public static GroovyCompiler createCompiler() {
        return GroovyManager.getInst().createCompiler();
    }

    /**
     * Освобождает компилятор и его ресурсы. Используется для временно
     * созданных компиляторов.
     */
    public static void destroyCompiler(GroovyCompiler compiler) {
        GroovyManager.getInst().destroyCompiler(compiler);
    }

    /**
     * Найти GroovyClazz по обычному классу
     *
     * @param cls какой класс интересует
     * @return null, если не найден
     */
    public static GroovyClazz findClazz(Class cls) {
        return GroovyManager.getInst().findClazz(cls);
    }

    /**
     * Проверка на изменные исходники groovy для всех зарегистрированных компиляторов.
     */
    public static void checkChangedResource() {
        GroovyManager.getInst().checkChangedResource();
    }

}
