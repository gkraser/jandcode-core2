package jandcode.core.dao;

import jandcode.commons.named.*;

/**
 * Описание dao-класса.
 * Имя - полное имя класса.
 */
public interface DaoClassDef extends INamed {

    /**
     * Оригинальный класс.
     */
    Class getCls();

    /**
     * Методы dao
     */
    NamedList<DaoMethodDef> getMethods();


}
