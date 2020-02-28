package jandcode.commons;


import jandcode.commons.cli.*;
import jandcode.commons.cli.impl.*;

/**
 * Утилиты для cli
 */
public class UtCli {

    /**
     * Создание экземпляра с параметрами командной строки.
     * <p>
     * Правила разбора:
     * <ul>
     * <li>опция начинается с '-'
     * <li>после первого ':' - параметр опции. Если он не указан - то считается равным ''
     * <li>если опция не начинается с '-', то она считается безымяным параметром и помещается в params
     * <li>если есть несколько одинаковых опций, то всегда рассматривается последняя
     * <li>имена опций являются регистрозависимыми.
     * </ul>
     */
    public static CliArgs createArgs(String[] args) {
        return new CliArgsParser().parse(args);
    }

    /**
     * Создание экземпляра форматтера помощи.
     */
    public static CliHelpFormatter createHelpFormatter() {
        return new CliHelpFormatterImpl();
    }

}
