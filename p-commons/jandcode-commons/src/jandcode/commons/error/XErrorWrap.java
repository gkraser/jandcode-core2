package jandcode.commons.error;

/**
 * Простая обертка для ошибок. Используется, когда нужно сгенерировать ошибку
 * в методе, который для этого не преднозначен, а ошибку делает.
 */
public class XErrorWrap extends RuntimeException {

    public XErrorWrap(Throwable cause) {
        super(cause);
    }

}
