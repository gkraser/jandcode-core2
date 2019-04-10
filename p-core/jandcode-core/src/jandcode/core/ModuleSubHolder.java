package jandcode.core;

/**
 * Подмножество хранилища модулей.
 * Позволяет иметь список модулей, которые зависят друг от друга.
 * Например для целей формирования списка модулей с определенными характеристиками.
 */
public interface ModuleSubHolder extends ModuleHolder {

    /**
     * Добавить модуль
     */
    void add(String moduleName);

    /**
     * Удалить модуль из этого списка.
     */
    void remove(String moduleName);

}
