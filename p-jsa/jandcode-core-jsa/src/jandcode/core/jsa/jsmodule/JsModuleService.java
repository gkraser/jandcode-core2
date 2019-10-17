package jandcode.core.jsa.jsmodule;

import jandcode.core.*;

import java.util.*;

/**
 * Сервис для js-модулей
 */
public interface JsModuleService extends Comp {

    /**
     * Разрешить require.
     *
     * @param basePath относительно какого
     * @param require  какой. Может быть относительным, содержать '*'.
     * @return список путей модулей
     */
    List<String> resolveRequire(String basePath, String require);

    /**
     * Разрешить имя модуля.
     *
     * @param name имя модуля, например 'jquery' или 'a/b/c/d'
     * @return полное имя модуля, например '_jsa/_node_modules/jquery/dist/jquery.js'
     * или 'a/b/c/d.js'
     */
    String resolveModuleName(String name);

    /**
     * Возвращает список модулей для указанного пути
     *
     * @param paths пути модулей через ',', могут содержать '*'
     */
    List<JsModule> getModules(String paths);

    /**
     * Найти модуль по имени
     *
     * @param path полное имя модуля
     * @return null, если не найден
     */
    JsModule findModule(String path);

    /**
     * Найти модуль по имени
     *
     * @param path полное имя модуля
     * @return ошибка, если не найден
     */
    JsModule getModule(String path);

    /**
     * Найти модуль по id
     *
     * @param id id модуля
     * @return null, если не найден
     */
    JsModule findModuleById(String id);

    /**
     * Найти модуль по id
     *
     * @param id id модуля
     * @return ошибка, если не найден
     */
    JsModule getModuleById(String id);

    /**
     * Является ли путь модулем
     */
    boolean isModule(String path);

}
