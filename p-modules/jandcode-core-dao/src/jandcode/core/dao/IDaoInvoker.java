package jandcode.core.dao;

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
     * Выполнить dao-метод.
     *
     * @param ctxIniter инициализатор контекста dao перед выполнением.
     *                  Может быть null, если не нужен
     * @param method    какой метод
     * @param args      аргументы
     * @return {@link DaoContext}, который использовался для выполнения dao
     */
    DaoContext invokeDao(DaoContextIniter ctxIniter, DaoMethodDef method, Object... args) throws Exception;

    /**
     * Создать экземпляр dao.
     * Все dao-методы будут выполнятся через механизм {@link DaoInvoker#invokeDao(DaoMethodDef, java.lang.Object...)}.
     * При вызове dao-метода создается другой экземпляр dao-класса.
     * Таким образом созданный экземпляр не что иное, как просто удобный способ
     * выполнить invokeMethod.
     */
    <A> A createDao(Class<A> cls);

}
