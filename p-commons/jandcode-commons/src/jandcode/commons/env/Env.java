package jandcode.commons.env;

/**
 * Среда исполнения (prod/dev)
 */
public interface Env {

    /**
     * Режим разработки.
     * Альтернатива: <code>!isProd()</code>.
     */
    boolean isDev();

    /**
     * Режим production.
     * Альтернатива: <code>!isDev()</code>.
     */
    boolean isProd();

    /**
     * Режим тестов.
     * Означает, что сейчас запускаются тесты.
     * Этот параметр устанавливается независито от
     * {@link Env#isProd()} и {@link Env#isDev()}
     */
    boolean isTest();

}
