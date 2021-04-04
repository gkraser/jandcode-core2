package jandcode.commons.cli;

/**
 * Опция командной строки, включая позиционные параметры.
 */
public interface CliOpt extends ICliOpt {

    /**
     * Имя опции. Можно указывать несколько значений через ',':
     * name("-d,--debug")
     */
    CliOpt names(String v);

    /**
     * Помощь по опции
     */
    CliOpt desc(String v);

    /**
     * Имя аргумента опции
     */
    CliOpt arg(String v);

    /**
     * При значении true - опция имеет аргумент с именем ARG.
     * При значении false - опция не имеет агрументов.
     */
    CliOpt arg(boolean v);

    /**
     * Значение аргумента по умолчанию.
     */
    CliOpt defaultValue(Object v);

    /**
     * Опция может повторятся несколько раз.
     */
    CliOpt multi(boolean v);

    /**
     * Опция обязательна
     */
    CliOpt required(boolean v);

}
