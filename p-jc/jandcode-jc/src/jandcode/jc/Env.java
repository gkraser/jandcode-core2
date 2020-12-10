package jandcode.jc;

/**
 * Среда исполнения
 */
public interface Env {

    /**
     * production режим. Можно учитывать при компиляции.
     */
    boolean isProd();

    /**
     * Установить/сбросить production режим.
     * Обычно явно устанавливается в jc product
     */
    void setProd(boolean v);

    /**
     * debug режим. Можно учитывать при компиляции.
     */
    boolean isDebug();

    /**
     * Установить/сбросить debug режим.
     * Обычно явно устанавливается в jc product
     */
    void setDebug(boolean v);

}
