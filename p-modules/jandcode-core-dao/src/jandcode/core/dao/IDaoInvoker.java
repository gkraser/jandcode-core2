package jandcode.core.dao;

import java.util.*;

/**
 * Интерфейс исполнителя dao
 */
public interface IDaoInvoker extends IDaoClassFactory {

    /**
     * Выполнить dao-метод.
     *
     * @param method какой метод
     * @param args   аргументы
     * @return то, что метод возвратит
     */
    Object invokeDao(DaoMethodDef method, Object... args) throws Exception;

    /**
     * Создать экземпляр dao.
     * Все dao-методы будут выполнятся через механизм {@link DaoInvoker#invokeDao(DaoMethodDef, java.lang.Object...)}.
     * При вызове dao-метода создается другой экземпляр dao-класса.
     * Таким образом созданный экземпляр не что иное, как просто удобный способ
     * выполнить invokeMethod.
     */
    <A> A createDao(Class<A> cls);

}
