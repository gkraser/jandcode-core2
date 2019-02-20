package jandcode.commons.error;

/**
 * Ошибка - маркер.
 * Этой ошибкой маскируются другие ошибки для пометки места/объекта, где ошибка произошла
 */
public class XErrorMark extends RuntimeException implements IErrorMark {

    public XErrorMark(Throwable cause, String message) {
        super(message, cause);
    }

}
