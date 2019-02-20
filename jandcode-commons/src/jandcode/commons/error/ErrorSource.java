package jandcode.commons.error;

/**
 * Описание источника ошибки в исходном тексте
 */
public interface ErrorSource {

    /**
     * Полное имя исходника. Обычно файл на диске.
     *
     * @return null, если неизвестен
     */
    String getSourceName();

    /**
     * Номер строки с ошибкой в исходном тексте
     *
     * @return -1, если неизвестен номер строки
     */
    int getLineNum();

    /**
     * Текст строки исходника, в которой произошла ошибка.
     *
     * @return null, если неизвестен
     */
    String getLineText();

    /**
     * Текст строки преобразованного исходника , в которой произошла ошибка.
     * Например для шаблонов.
     *
     * @return null, если неизвестен
     */
    String getLineTextPrepared();

}
