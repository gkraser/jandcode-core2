package jandcode.commons.cli;

/**
 * Интерфейс команды для {@link CliLauncher}.
 */
public interface CliCmd extends CliConfigure {

    /**
     * Выполнить команду
     */
    void exec() throws Exception;

}
