package jandcode.commons.ansifer4;

/**
 * Интерфейс для движка ansi-консоли
 */
public interface AnsiferEngine {

    /**
     * Инициализация.
     *
     * @return true если все прошло успешно
     */
    boolean install();

    /**
     * Деинициализация.
     *
     * @return true если все прошло успешно
     */
    boolean uninstall();

}
