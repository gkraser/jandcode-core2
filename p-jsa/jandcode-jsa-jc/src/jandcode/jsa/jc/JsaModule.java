package jandcode.jsa.jc;

import jandcode.commons.moduledef.*;
import jandcode.commons.named.*;
import jandcode.jc.*;

import java.util.*;

/**
 * Модуль jsa.
 * Представление lib с модулем для jsa.
 * Имя {@link JsaModule#getName()} - это имя библиотеки, где модуль описан.
 */
public interface JsaModule extends INamed {

    /**
     * Определение модуля
     */
    ModuleDef getModuleDef();

    /**
     * Путь до корня исходников модуля.
     * Входит в {@link JsaModule#getResolvePaths()}.
     */
    String getSrcPath();

    /**
     * Ссылка на библиотеку
     */
    Lib getLib();

    /**
     * Список путей resolve для nodejs-модулей
     */
    List<String> getResolvePaths();

    /**
     * Зависимости для node
     * (см {@link JsaProject#nodeDepends}
     */
    Map<String, String> getNodeDepends();

    /**
     * Определения задач для gulp
     * (см {@link JsaProject#gulpTask(Map)}
     */
    Map<String, Object> getGulpTasks();

}
