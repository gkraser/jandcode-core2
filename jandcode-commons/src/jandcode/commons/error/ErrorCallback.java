package jandcode.commons.error;

/**
 * Для обработки ошибок в массовых операциях
 */
public interface ErrorCallback {

    /**
     * Вызывается при ошибке
     *
     * @param e ошибка
     * @return вернуть true, если нужно продолжить
     */
    boolean onErrorCallback(Throwable e);

}
