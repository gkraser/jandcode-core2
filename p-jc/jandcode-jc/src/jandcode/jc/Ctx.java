package jandcode.jc;

/**
 * Контекст исполнения jc.
 * Содержит и управляет всем окружением.
 */
public interface Ctx extends IEvents, ILog, IScripts, IProjects,
        IAnts, ILibs, ITempdir, IServices, IEnv {

    /**
     * Возвращает true, если это runtime контекст.
     * В таком контексте мы считаем, что исходники компилировать не нужно,
     * т.к. они уже так или иначе скомпилированы и в classpath присутсвуют.
     */
    boolean isRuntimeCtx();

    /**
     * Конфигурация контекста.
     */
    JcConfig getConfig();

    /**
     * Установить конфигурацию.
     * Выполняется только один раз.
     * В процессе конфигурирования возможны загрузки проектов.
     */
    void applyConfig(JcConfig config);

}
