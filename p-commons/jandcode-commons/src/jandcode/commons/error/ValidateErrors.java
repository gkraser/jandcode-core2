package jandcode.commons.error;

import jandcode.commons.error.impl.*;

import java.util.*;

/**
 * Объект для накопления ошибок валидации
 */
public interface ValidateErrors {

    /**
     * Создать экземпляр {@link ValidateErrors}
     */
    static ValidateErrors create() {
        return new ValidateErrorsImpl();
    }

    /**
     * Накопленные ошибки
     */
    List<ValidateErrorInfo> getErrorInfos();

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
     * @param error информация об ошибке
     */
    void addError(ValidateErrorInfo error);

    /**
     * Добавить ошибку
     *
     * @param message сообщение
     * @param field   имя поля, если ошибка привязана к полю
     * @param data    связанный объект данных
     */
    void addError(CharSequence message, String field, Object data);

    /**
     * Добавить ошибку
     *
     * @param message сообщение
     * @param field   имя поля, если ошибка привязана к полю
     */
    default void addError(CharSequence message, String field) {
        addError(message, field, null);
    }

    /**
     * Добавить ошибку
     *
     * @param message сообщение
     */
    default void addError(CharSequence message) {
        addError(message, null, null);
    }

    //////

    /**
     * Добавить ошибку
     *
     * @param error информация об ошибке
     */
    default void addErrorFatal(ValidateErrorInfo error) {
        addError(error);
        checkErrors();
    }

    /**
     * Добавить фатальную ошибку.
     * Сразу вызывется checkErrors
     *
     * @param message сообщение
     * @param field   имя поля, если ошибка привязана к полю
     * @param data    связанный объект данных
     */
    default void addErrorFatal(CharSequence message, String field, Object data) {
        addError(message, field, data);
        checkErrors();
    }

    /**
     * Добавить фатальную ошибку.
     * Сразу вызывется checkErrors
     *
     * @param message сообщение
     * @param field   имя поля, если ошибка привязана к полю
     */
    default void addErrorFatal(CharSequence message, String field) {
        addError(message, field);
        checkErrors();
    }

    /**
     * Добавить фатальную ошибку.
     * Сразу вызывется checkErrors
     *
     * @param message сообщение
     */
    default void addErrorFatal(CharSequence message) {
        addError(message);
        checkErrors();
    }

}
