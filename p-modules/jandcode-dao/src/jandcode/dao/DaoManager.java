package jandcode.dao;

import jandcode.core.*;

/**
 * Менеджер dao
 */
public interface DaoManager extends Comp, BeanFactoryOwner, IBeanIniter {

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
     * Создать экземпляр dao.
     * Все dao-методы будут выполнятся через механизм {@link DaoManager#invokeMethod(jandcode.dao.DaoMethodDef, java.lang.Object...)}.
     * При вызове dao-метода создается другой экземпляр dao-класса.
     * Таким образом созданный экземпляр не что иное, как просто удобный способ
     * выполнить invokeMethod.
     */
    <A> A createDao(Class<A> cls);


}
