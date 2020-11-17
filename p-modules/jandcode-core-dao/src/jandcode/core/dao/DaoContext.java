package jandcode.core.dao;

import jandcode.core.*;

/**
 * Контекст исполнения dao
 */
public interface DaoContext extends IAppLink, BeanFactoryOwner {

    /**
     * Экземпляр dao
     */
    Object getDaoInst();

    /**
     * Какой метод исполняем
     */
    DaoMethodDef getDaoMethodDef();

    /**
     * Кто исполняет
     */
    DaoInvoker getDaoInvoker();

    /**
     * Время начала выполнения dao.
     */
    long getStartTime();

    /**
     * Результат выполнения метода dao, если dao-метод удачно закончил выполнятся
     */
    Object getResult();

    /**
     * Пойманная ошибка, если dao закончил выполнение с ошибкой
     */
    Throwable getException();

}
