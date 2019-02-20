package jandcode.jc;

/**
 * Поддержка логирования в скриптах
 */
public interface Log {

    /**
     * Вывод лог-сообщения
     */
    void info(Object msg);

    /**
     * Синоним для {@link Log#info(java.lang.Object)}
     */
    void call(Object msg);

    /**
     * Вывод лог-сообщения, которое будет видимо в режиме verbose.
     */
    void debug(Object msg);

    /**
     * Вывод warning лог-сообщения.
     */
    void warn(Object msg);

    /**
     * true - включен verbose
     */
    boolean isVerbose();

    void setVerbose(boolean v);

}
