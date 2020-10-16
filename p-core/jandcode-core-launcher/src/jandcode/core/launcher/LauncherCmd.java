package jandcode.core.launcher;

import jandcode.commons.cli.*;
import jandcode.core.*;

/**
 * Команда для запуска в launcher с командной строки
 */
public interface LauncherCmd extends Comp {

    /**
     * Аргументы командной строки
     */
    CliArgs getArgs();

    /**
     * Установить аргументы командной строки.
     * Устанавливается ссылка на args!
     *
     * @param args аргументы
     */
    void setArgs(CliArgs args);

    /**
     * При значении true требуется более подробный вывод
     */
    boolean isVerbose();

    /**
     * Построить помощь по команде
     */
    void help(HelpBuilder h) throws Exception;

    /**
     * Выполнить команду
     */
    void exec() throws Exception;

}
