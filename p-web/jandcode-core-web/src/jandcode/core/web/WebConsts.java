package jandcode.core.web;

/**
 * Константы
 */
public class WebConsts {

    /**
     * Индексный файл gsp для папки
     */
    public static final String FILE_INDEX_GSP = "index.gsp";

    /**
     * Индексный файл html для папки
     */
    public static final String FILE_INDEX_HTML = "index.html";

    /**
     * Префикс для ajax-сообщений об ошибках
     */
    public static final String ERROR_AJAX_PREFIX = "ERROR_AJAX:";

    /**
     * Имя сервлета web по умолчанию
     */
    public static final String WEB_SERVLET_NAME = "jandcode-core-web";


    // атрибуты запроса

    /**
     * Атрибут запроса: pathInfo для action
     */
    public static final String a_actionPathInfo = "actionPathInfo";
    /**
     * Атрибут запроса: имя метода для action
     */
    public static final String a_actionMethod = "actionMethod";
    /**
     * Атрибут запроса: pathInfo для метода action
     */
    public static final String a_actionMethodPathInfo = "actionMethodPathInfo";

}
