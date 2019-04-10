package jandcode.jc;

/**
 * Среда исполнения
 */
public interface Env {

    /**
     * production режим. Можно учитывать компиляции.
     */
    boolean isProd();

    /**
     * Установить/сбросить production режим.
     * Обычно явно устанавливается в jc product
     */
    void setProd(boolean v);

}
