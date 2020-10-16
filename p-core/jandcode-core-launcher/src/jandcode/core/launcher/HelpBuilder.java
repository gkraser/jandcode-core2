package jandcode.core.launcher;

/**
 * Построитель помощи
 */
public interface HelpBuilder {

    /**
     * Параметры опции
     */
    OptBuilder opt(String name);

    /**
     * Параметры опции
     */
    OptBuilder opt(String name, String help);

    /**
     * Параметры опции
     */
    OptBuilder opt(String name, String help, String argName);

    /**
     * Параметры опции
     */
    OptBuilder opt(String name, String help, String argName, Object defaultArgValue);

    /**
     * Параметры опции
     */
    OptBuilder opt(String name, String help, boolean hasArg);

    /**
     * Параметры опции
     */
    OptBuilder opt(String name, String help, boolean hasArg, Object defaultArgValue);

    /**
     * Описание
     */
    void desc(String v);

}
