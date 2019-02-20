package jandcode.jc;

import jandcode.commons.groovy.*;

import java.util.*;

/**
 * gsp-скрипт
 */
public interface IGspScript extends IGspTemplate, IGspGen {

    /**
     * Генерация в указанный файл
     *
     * @param outFileName куда генерировать
     * @param args        аргументы для генерации
     */
    void generate(String outFileName, Map args);

    /**
     * Генерация в указанный файл
     *
     * @param outFileName куда генерировать
     */
    void generate(String outFileName);

    /**
     * Кодировка генерируемых файлов. По умолчанию utf8.
     */
    String getCharset();

    /**
     * Установить кодировку генерируемых файлов
     */
    void setCharset(String charset);

}
