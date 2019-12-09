package jandcode.core.dao;

import jandcode.core.*;

import java.util.*;

/**
 * Сервис для dao
 */
public interface DaoService extends Comp {

    /**
     * Получить {@link DaoInvoker} по имени
     */
    DaoInvoker getDaoInvoker(String name);

    /**
     * Имена зарегистрированных {@link DaoInvoker}
     */
    Collection<String> getDaoInvokerNames();

    /**
     * Получить {@link DaoHolder} по имени
     */
    DaoHolder getDaoHolder(String name);

    /**
     * Имена зарегистрированных {@link DaoHolder}
     */
    Collection<String> getDaoHolderNames();

    /**
     * Возвращает описание dao-класса
     *
     * @param cls dao-класс
     */
    DaoClassDef getDaoClassDef(Class cls);

}
