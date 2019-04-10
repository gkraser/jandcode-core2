package jandcode.core;

import jandcode.commons.error.*;

/**
 * Хранилище модулей.
 */
public interface ModuleHolder extends Iterable<Module> {

    /**
     * Поиск модуля по имени
     *
     * @param name имя модуля
     * @return null, если не найден
     */
    Module find(String name);

    /**
     * Поиск модуля по имени
     *
     * @param name имя модуля
     * @return ошибка, если не найден
     */
    default Module get(String name) {
        Module m = find(name);
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
