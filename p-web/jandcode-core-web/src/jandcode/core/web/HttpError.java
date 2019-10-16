package jandcode.core.web;

/**
 * Обертка для http-кодов возврата (sendError).
 * Этот exception нужно генерировать при необходимости вернуть код возврата клиенту.
 */
public class HttpError extends RuntimeException {

    private int code;

    public HttpError(int code) {
        super("");
        this.code = code;
    }

    public HttpError(int code, String message) {
        super(message);
        this.code = code;
    }

    public int getCode() {
        return code;
    }

}
