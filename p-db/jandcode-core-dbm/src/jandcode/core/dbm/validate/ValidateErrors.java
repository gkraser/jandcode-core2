package jandcode.core.dbm.validate;

import java.util.*;

/**
 * Объект для накопления ошибок валидации
 */
public interface ValidateErrors {

    /**
     * Информация о конкретной ошибке
     */
    interface ErrorInfo {

        /**
         * Сообщение об ошибке
         */
        String getMessage();

        /**
         * Связанный объект данных
         */
        Object getData();

        /**
         * Имя поля, если ошибка привязана к полю
         */
        String getFieldName();

    }

    //////

    /**
     * Накопленные ошибки
     */
    List<ErrorInfo> getErrorInfos();

    /**
     * Очистить ошибки
     */
    void clearErrors();

    /**
     * Есть ли ошибки
     */
    boolean hasErrors();

    /**
     * Есть ли ошибок больше, чем size
     */
    boolean hasErrors(int size);

    /**
     * Если есть ошибки, сгенерить XErrorValidate
     */
    void checkErrors();

    //////

    /**
     * Добавить ошибку
     *
     * @param message   сообщение
     * @param data      связанный объект данных
     * @param fieldName имя поля, если ошибка привязана к полю
     */
    void addError(Object message, Object data, String fieldName);

    /**
     * Добавить ошибку
     *
     * @param message сообщение
     */
    void addError(Object message);

    /**
     * Добавить ошибку
     *
     * @param message сообщение
     * @param data    связанный объект данных
     */
    void addError(Object message, Object data);

    /**
     * Добавить ошибку
     *
     * @param message   сообщение
     * @param fieldName имя поля, если ошибка привязана к полю
     */
    void addError(Object message, String fieldName);

    //////

}
