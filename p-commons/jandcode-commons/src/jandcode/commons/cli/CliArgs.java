package jandcode.commons.cli;

import jandcode.commons.variant.*;

import java.util.*;

/**
 * Аргументы командной строки
 */
public interface CliArgs extends IVariantMap {

    /**
     * Оригинальные аргументы, которые были распарзены.
     */
    String[] getArgs();

    /**
     * Параметры (которые без опций)
     */
    List<String> getParams();


}
