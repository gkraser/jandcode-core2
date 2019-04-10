package jandcode.jc;

/**
 * Интерфейс провайдера библиотек
 */
public interface ILibProvider {

    /**
     * Базовый каталог для провайдера
     */
    String getPath();

    /**
     * Найти библиотеку по имени
     */
    Lib findLib(String name);

    /**
     * Список библиотек провайдера. Возвращается копия списка.
     */
    ListLib getLibs();

}
