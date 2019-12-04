package jandcode.core.dao;

/**
 * Параметры для вызова методов фильтра dao
 */
public interface DaoFilterParams {

    /**
     * Контекст исполнения dao
     */
    DaoContext getContext();

    /**
     * Экземпляр dao
     */
    Object getDaoInst();

    /**
     * Результат выполнения метода dao, если dao-метод удачно закончил выполнятся
     */
    Object getResult();

    /**
     * Пойманная ошибка, если dao закончил выполнение с ошибкой
     */
    Throwable getException();

}
