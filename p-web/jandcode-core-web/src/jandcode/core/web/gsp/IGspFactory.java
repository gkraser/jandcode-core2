package jandcode.core.web.gsp;

/**
 * Фабрика gsp
 */
public interface IGspFactory {

    /**
     * Создать экземпляр gsp по имени
     */
    Gsp createGsp(String gspName) throws Exception;

}
