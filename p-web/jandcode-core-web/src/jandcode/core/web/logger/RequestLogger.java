package jandcode.core.web.logger;

import jandcode.core.web.*;

/**
 * Логгер для запроса. Используется в WebService.
 */
public interface RequestLogger {

    /**
     * Превратить запрос в строку лога
     */
    String toString(Request request);

    /**
     * Начало запроса
     */
    void logStart(Request request);

    /**
     * Конец запроса
     */
    void logStop(Request request);

    /**
     * Ошибка
     */
    void logError(Request request, Throwable e);

    /**
     * Ошибка HttpError
     */
    void logHttpError(Request request, HttpError e);

    /**
     * Ошибка HttpRedirect
     */
    void logHttpRedirect(Request request, HttpRedirect e);

}
