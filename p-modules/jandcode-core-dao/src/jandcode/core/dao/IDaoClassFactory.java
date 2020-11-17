package jandcode.core.dao;

/**
 * Фабрика классов для dao.
 * Позволяет из класса извлечь описание dao-методов.
 */
public interface IDaoClassFactory {

    /**
     * Возвращает описание dao-класса
     *
     * @param cls dao-класс
     */
    DaoClassDef getDaoClassDef(Class cls);


}
