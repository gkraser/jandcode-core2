package jandcode.jc;

import jandcode.commons.io.*;
import jandcode.commons.variant.*;

/**
 * Генератор gsp.
 * Собержит методы, которые используются в процессе работы генератора.
 */
public interface IGspGen {

    /**
     * Аргументы, переданные при вызове генерайции в
     * {@link IGspScript#generate(java.lang.String, java.util.Map)}.
     */
    IVariantMap getArgs();

    /**
     * Сменить текущий файл. Предыдущий файл закрывается.
     *
     * @param filename имя файла относительно базового каталога
     * @param append   добавлять ли в файл, если он существует
     */
    void changeFile(String filename, boolean append);

    /**
     * см. {@link IGspGen#changeFile(java.lang.String, boolean)},
     * где append=false
     */
    void changeFile(String filename);

    /**
     * Сменить текущий файл, поместив предыдущий в стек.
     * Предыдущий файл остается открытым.
     * К нему можно вернутся вызвав {@link IGspGen#popFile()}.
     *
     * @param filename имя файла относительно базового каталога
     * @param append   добавлять ли в файл, если он существует
     */
    void pushFile(String filename, boolean append);

    /**
     * см. {@link IGspGen#pushFile(java.lang.String, boolean)},
     * где append=false
     */
    void pushFile(String filename);

    /**
     * Извлечь файл из стека и сделать его текущим.
     * Текущий закрывается.
     */
    void popFile();

    /**
     * Текущий writer {@link IndentWriter}.
     * Можно использовать для установки отступов.
     */
    IndentWriter getWriter();

    /**
     * Полное имя текущего файла
     */
    String getCurrentFile();

    /**
     * Полное имя базового каталога, в который осуществляется генерация
     */
    String getOutDir();

}
