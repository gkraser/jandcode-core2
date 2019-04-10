package jandcode.commons;

import jandcode.commons.error.*;
import jandcode.commons.error.impl.*;

/**
 * Утилиты для обработки ошибок
 */
public class UtError {

    private static ErrorConvertorHolder errorConvertorHolder = new ErrorConvertorHolder();

    static {
        addErrorConvertor(new ErrorConvertorDefault());
    }

    /**
     * Конвертор ошибок, собранный из всех конверторов, которые быди добавлены
     * через {@link UtError#addErrorConvertor(ErrorConvertor)}.
     */
    public static ErrorConvertor getErrorConvertor() {
        return errorConvertorHolder;
    }

    /**
     * Добавление конвертора ошибок.
     */
    public static void addErrorConvertor(ErrorConvertor errorConvertor) {
        errorConvertorHolder.getItems().add(0, errorConvertor);
    }

    /**
     * Создание экземпляра {@link ErrorInfo} для Throwable
     *
     * @param e ошибка
     */
    public static ErrorInfo createErrorInfo(Throwable e) {
        ErrorInfoImpl z = new ErrorInfoImpl();
        z.setException(e);
        return z;
    }

    /**
     * Возвращает true, если e ошибка ErrorValidate
     */
    public static boolean isErrorValidate(Throwable e) {
        ErrorInfo ei = createErrorInfo(e);
        for (Throwable e1 : ei.getExceptions()) {
            if (e1 instanceof ErrorValidate) {
                return true;
            }
        }
        return false;
    }

    /**
     * Возвращает ErrorValidate, если e ошибка валидации. Иначе возвращает null.
     */
    public static ErrorValidate getErrorValidate(Throwable e) {
        ErrorInfo ei = createErrorInfo(e);
        for (Throwable e1 : ei.getExceptions()) {
            if (e1 instanceof ErrorValidate) {
                return (ErrorValidate) e1;
            }
        }
        return null;
    }

}
