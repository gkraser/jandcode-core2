package jandcode.xcore;

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
