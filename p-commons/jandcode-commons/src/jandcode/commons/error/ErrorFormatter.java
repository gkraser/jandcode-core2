package jandcode.commons.error;

/**
 * Интерфейс форматтера ошибок для показа на консоле или еще где-нибудь.
 */
public interface ErrorFormatter {

    /**
     * Показывать ли исходники скриптов в сообщении об ошибке
     */
    public void setShowSource(boolean showSource);

    /**
     * Показывать ли стек в сообщении об ошибке
     */
    public void setShowStack(boolean showStack);

    /**
     * Показывать ли полный стек в сообщении об ошибке (только если включен
     * показ стека)
     */
    public void setShowFullStack(boolean showFullStack);

    /**
     * Получить текст ошибки по ErrorInfo
     */
    public String getMessage(ErrorInfo e);

    /**
     * Получить текст ошибки по Throwable
     */
    public String getMessage(Throwable e);

}
