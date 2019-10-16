package jandcode.core.web.gsp;

import java.io.*;

/**
 * Текстовый буфер
 */
public interface ITextBuffer {

    /**
     * Вывести содержимое в указанный writer
     */
    void writeTo(Writer w) throws Exception;

    /**
     * Содержимое в виде строки
     */
    String toString();

    /**
     * Вывести значение в буфер.
     *
     * @param s что вывести
     * @return самого себя
     */
    ITextBuffer out(Object s);

    /**
     * Проверка на пустой буфер
     */
    boolean isEmpty();

    /**
     * Проверка, что буфер состоит только из пробельных символов (включая переводы строк
     * и табуляцию).
     */
    boolean isWhite();

}
