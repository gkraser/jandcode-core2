package jandcode.core.dao;

import jandcode.core.*;

import java.util.*;

/**
 * Хранилище зарегистрированных dao
 */
public interface DaoHolder extends Comp {

    /**
     * Выполнить dao-метод.
     *
     * @param name какой метод
     * @param args аргументы
     * @return то, что метод возвратит
     */
    Object invokeDao(String name, Object... args) throws Exception;

    /**
     * Список всех зарегистрированных dao.
     * Только для чтения.
     */
    Collection<DaoHolderItem> getItems();

    /**
     * Какой {@link DaoManager} используется для выполнения.
     */
    String getDaoManagerName();

}
