package jandcode.jc;

/**
 * Интерфейс для тех, кто может собирать зависимости.
 * Используется при сборке всех зависимостей со всех проектов.
 */
public interface ILibDependsGrab {

    /**
     * Добавить в объект dep нужные зависимости.
     * Если объект в добавок реализует ILibDepends,
     * то их можно не добавлять.
     *
     * @param deps куда добавлять
     */
    void grabDepends(LibDepends deps);

}
