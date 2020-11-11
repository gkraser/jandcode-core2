package jandcode.core.jsa.jc;

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
     * Каталог jc-data/webroot.
     * Если он отсутствует в модуле, то возвращает null.
     */
    String getJcDataWebrootPath();

    /**
     * Ссылка на библиотеку
     */
    Lib getLib();

    /**
     * Список путей resolve для nodejs-модулей
     */
    List<String> getResolvePaths();

    /**
     * Определения задач для gulp
     * (см {@link JsaJavaProject#gulpTask(Map)}
     */
    Map<String, Object> getGulpTasks();

    /**
     * Зависимости для nodejs
     * (см {@link JsaJavaProject#getNodeJsDepends()}
     */
    List<String> getNodeJsDepends();

}
