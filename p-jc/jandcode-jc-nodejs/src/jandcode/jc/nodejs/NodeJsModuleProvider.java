package jandcode.jc.nodejs;

import jandcode.commons.named.*;

/**
 * Провайдер модулей nodejs
 */
public interface NodeJsModuleProvider {

    /**
     * Базовый каталог для провайдера
     */
    String getPath();

    /**
     * Найти модуль по имени
     */
    NodeJsModule findModule(String name);

    /**
     * Список модулей провайдера. Возвращается копия списка.
     */
    NamedList<NodeJsModule> getModules();

}
