package jandcode.core;

/**
 * Инициализатор объекта.
 * Вызывается фабрикой
 */
public interface IBeanIniter {

    /**
     * Инициализировать объект.
     */
    void beanInit(Object inst);

}
