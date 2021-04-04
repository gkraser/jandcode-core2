package jandcode.commons.cli;

/**
 * Расширение {@link CliLauncher}.
 */
public interface CliExtension extends CliConfigure {

    default void cliConfigure(CliDef b) {
        // ничего не делаем по умолчанию
    }

    /**
     * Настроить команду перед ее выполнением.
     */
    void beforeCmdExec(CliCmd cmd);

}
