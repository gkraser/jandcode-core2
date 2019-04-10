package jandcode.commons.ansifer.impl;

/**
 * Интерфейс для провайдеров Ansifer
 */
public interface AnsiferProvider {

    /**
     * Инициализация консоли
     */
    boolean install();

    /**
     * Деинициализация консоли
     */
    boolean uninstall();

    /**
     * Обрамление строки указанным стилем
     */
    String style(AnsiferStyle style, String s);


}
