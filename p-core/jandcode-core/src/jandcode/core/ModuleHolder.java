package jandcode.core;

import jandcode.commons.error.*;

/**
 * Хранилище модулей.
 */
public interface ModuleHolder extends Iterable<ModuleInst> {

    /**
     * Поиск модуля по имени
     *
     * @param name имя модуля
     * @return null, если не найден
     */
    ModuleInst find(String name);

    /**
     * Поиск модуля по имени
     *
     * @param name имя модуля
     * @return ошибка, если не найден
     */
    default ModuleInst get(String name) {
        ModuleInst m = find(name);
        if (m == null) {
            throw new XError("Модуль {0} не найден", name);
        }
        return m;
    }

    /**
     * Создать пустое подмножество хранилища.
     */
    ModuleSubHolder createSubHolder();

}
