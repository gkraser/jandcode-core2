package jandcode.core.dao;

import jandcode.core.*;

import java.util.*;

/**
 * Сервис для dao
 */
public interface DaoService extends Comp {

    /**
     * Получить {@link DaoManager} по имени
     */
    DaoManager getDaoManager(String name);

    /**
     * Имена зарегистрированных {@link DaoManager}
     */
    Collection<String> getDaoManagerNames();

    /**
     * Получить {@link DaoHolder} по имени
     */
    DaoHolder getDaoHolder(String name);

    /**
     * Имена зарегистрированных {@link DaoHolder}
     */
    Collection<String> getDaoHolderNames();

}
