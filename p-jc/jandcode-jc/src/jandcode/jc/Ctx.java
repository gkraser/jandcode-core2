package jandcode.jc;

/**
 * Контекст исполнения jc.
 * Содержит и управляет всем окружением.
 */
public interface Ctx extends IEvents, ILog, IScripts, IProjects,
        IAnts, ILibs, ITempdir, IServices, IEnv {

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

    /**
     * Map с набором переменных проекта.
     * Можно свободно писать и читать.
     */
    Vars getVars();

}
