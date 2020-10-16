package jandcode.core.launcher;

/**
 * Построитель опции
 */
public interface OptBuilder {

    /**
     * Помощь по опции
     */
    OptBuilder desc(String v);

    /**
     * Имя аргумента опции
     */
    OptBuilder arg(String v);

    /**
     * При значении true - опция имеет аргумент с именем ARG
     */
    OptBuilder arg(boolean v);

    /**
     * Значение аргумента по умолчанию.
     */
    OptBuilder defaultArgValue(Object v);


}
