package jandcode.commons.cli;

import jandcode.commons.variant.*;

import java.util.*;

/**
 * Аргументы командной строки
 */
public interface CliArgs extends IVariantMap {

    /**
     * Значение для опции бер аргументов.
     * Опция -z эквивалентна -z:1
     */
    String DEFAULT_OPT_VALUE = "1";

    /**
     * Оригинальные аргументы, которые были распарзены.
     */
    String[] getArgs();

    /**
     * Параметры (которые без опций)
     */
    List<String> getParams();


}
