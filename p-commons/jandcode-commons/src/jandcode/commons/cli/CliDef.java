package jandcode.commons.cli;

/**
 * Описание командной строки.
 */
public interface CliDef extends ICliDef {

    /**
     * Задать описание команды
     */
    CliDef desc(String v);

    /**
     * Задать имя команды
     */
    CliDef cmdName(String v);

    /**
     * Задать подвал
     */
    CliDef footer(String v);


    ////// options

    /**
     * Поучить опцию по ключу. Создать, если не существует.
     *
     * @param key уникальный ключ опции
     */
    CliOpt opt(String key);

    /**
     * Удалить опцию
     */
    void removeOpt(String key);


    ////// other

    /**
     * Объеденить с указанным описанием.
     * Если в этом объекте не установлено свойство, оно берется из указанного.
     * Опции копируются и перекрывают существующие.
     */
    void joinFrom(CliDef cliDef);

}
