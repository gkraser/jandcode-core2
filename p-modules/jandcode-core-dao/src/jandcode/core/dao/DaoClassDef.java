package jandcode.core.dao;

import jandcode.commons.named.*;

/**
 * Описание dao-класса.
 * Имя - полное имя класса.
 */
public interface DaoClassDef extends INamed {

    /**
     * Оригинальный класс. Этот класс используется для получения методов.
     */
    Class getCls();

    /**
     * Класс для создания экземпляра. Может не совпадать с getCls()
     */
    Class getClsInst();

    /**
     * Методы dao
     */
    NamedList<DaoMethodDef> getMethods();

}
