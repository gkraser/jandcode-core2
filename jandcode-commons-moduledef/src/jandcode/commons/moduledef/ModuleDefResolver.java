package jandcode.commons.moduledef;

import jandcode.commons.error.*;

/**
 * Ресолвер для ModuleDef
 */
public interface ModuleDefResolver {

    /**
     * Найти описание модуля по имени
     *
     * @param moduleName имя модуля
     * @return null, если модуль не найден
     */
    ModuleDef findModuleDef(String moduleName);

    /**
     * Найти описание модуля по имени
     *
     * @param moduleName имя модуля
     * @return ошибка, если модуль не найден
     */
    ModuleDef getModuleDef(String moduleName);

    /**
     * Добавить каталог, в котором имеется информация о модулях.
     * В частности из такого каталога можно загрузить описания
     * модулей в исходниках.
     *
     * @param workDir каталог
     */
    void addWorkDir(String workDir);

}
