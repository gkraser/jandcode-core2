package jandcode.core.dao;

import jandcode.core.*;

import java.util.*;

/**
 * Менеджер dao
 */
public interface DaoManager extends Comp, IBeanFactoryOwner {

    /**
     * Выполнить dao-метод.
     *
     * @param method какой метод
     * @param args   аргументы
     * @return то, что метод возвратит
     * @throws Exception
     */
    Object invokeMethod(DaoMethodDef method, Object... args) throws Exception;

    /**
     * Возвращает описание dao-класса
     *
     * @param cls dao-класс
     */
    DaoClassDef getDaoClassDef(Class cls);

    /**
     * Создать экземпляр dao.
     * Все dao-методы будут выполнятся через механизм {@link DaoManager#invokeMethod(DaoMethodDef, java.lang.Object...)}.
     * При вызове dao-метода создается другой экземпляр dao-класса.
     * Таким образом созданный экземпляр не что иное, как просто удобный способ
     * выполнить invokeMethod.
     */
    <A> A createDao(Class<A> cls);

    /**
     * Список зарегистрированных фильтров.
     * Только для чтения.
     */
    Collection<DaoFilter> getDaoFilters();

}
