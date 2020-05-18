package jandcode.core.jsa.jc;

/**
 * Провайдер библиотек nodejs
 */
public interface NodeJsLibProvider {

    /**
     * Базовый каталог для провайдера
     */
    String getPath();

    /**
     * Найти библиотеку по имени
     */
    NodeJsLib findLib(String name);

    /**
     * Список библиотек провайдера. Возвращается копия списка.
     */
    NodeJsLibList getLibs();

}
