package jandcode.jc;

/**
 * Поддержка логгирования
 */
public interface ILog {

    /**
     * Логгер
     */
    Log getLog();

    /**
     * Алиас для {@link Log#info(java.lang.Object)}
     */
    void log(Object msg);

    /**
     * Алиас для {@link Log#debug(java.lang.Object)}
     */
    void debug(Object msg);

    /**
     * Алиас для {@link Log#warn(java.lang.Object)}
     */
    void warn(Object msg);

}
