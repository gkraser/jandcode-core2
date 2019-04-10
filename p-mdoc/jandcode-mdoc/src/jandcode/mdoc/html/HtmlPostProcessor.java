package jandcode.mdoc.html;

import jandcode.mdoc.builder.*;

/**
 * Пост-обработка html файла
 */
public interface HtmlPostProcessor {

    /**
     * Обработать html-текст.
     *
     * @param html    исходный текст
     * @param outFile для какого файла
     * @return обработанный текст
     */
    String process(String html, OutFile outFile);

}
