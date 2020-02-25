package jandcode.commons.cli;

/**
 * Формирование помощи по опциям.
 */
public interface CliHelpFormatter {

    /**
     * Стиль ansi для вывода имени опции
     */
    String STYLE_OPT_NAME = "opt-name";

    /**
     * Стиль ansi для вывода поможи для опции
     */
    String STYLE_OPT_HELP = "opt-help";

    /**
     * Добавить опцию. К началу имени опции добавляется '-'.
     *
     * @param name    имя
     * @param help    помощь
     * @param argName имя аргумента. null или пустая строка - аргумента нет.
     */
    void addOpt(String name, String help, String argName);

    /**
     * Добавить опцию.
     * <p>
     * см {@link CliHelpFormatter#addOpt(java.lang.String, java.lang.String, java.lang.String)},
     * где argName = ARG, если hasArg=true
     */
    void addOpt(String name, String help, boolean hasArg);

    /**
     * Добавить опцию без аргумента
     * см {@link CliHelpFormatter#addOpt(java.lang.String, java.lang.String, java.lang.String)},
     * где argName = null
     */
    void addOpt(String name, String help);

    /**
     * Добавить команду. Имя используеьтся как есть (не добавляется '-')
     *
     * @param name имя
     * @param help помощь
     */
    void addCmd(String name, String help);

    /**
     * true - помощь по команде печатать на следующей строке
     */
    void setHelpToNextLine(boolean helpToNextLine);

    /**
     * При значении true разрешвается использовать ansi-расцветку.
     */
    void setAnsi(boolean v);

    /**
     * Установить стили для ansi-вывода.
     */
    void setAnsiStyle(String optNameStyle, String optHelpStyle);

    /**
     * Построить текстовое представление помощи
     */
    String build();

}
