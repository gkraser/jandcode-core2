package jandcode.commons.moduledef;

/**
 * Константы для модулей
 */
public class ModuleDefConsts {

    /**
     * Файл с реестром модулей в проекте.
     */
    public static final String FILE_REGISTRY_MODULE_DEF = "_jc/registry-module-def.cfx";

    /**
     * Имя файла с описанием модуля
     */
    public static final String FILE_MODULE_CONF = "module.cfx";

    /**
     * Атрибут в jar-манифесте с именами модулей, которые содержатся в библиотке.
     */
    public static final String MANIFEST_MODULES = "Jandcode-Modules";

    /**
     * Функция загрузки conf: зависимость модуля
     */
    public static final String CONF_FUNC_DEPENDS = "depends";

}
