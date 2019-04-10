package jandcode.mdoc.topic;

import jandcode.commons.variant.*;
import jandcode.mdoc.source.*;

/**
 * Статья
 */
public interface Topic {

    /**
     * id статьи = {@link SourceFile#getPath()} без расширения.
     */
    String getId();

    /**
     * Исходный файл, из которого появилась статья.
     */
    SourceFile getSourceFile();

    /**
     * Тело статьи в виде html-текста.
     */
    String getBody();

    /**
     * Заголовок статьи
     */
    String getTitle();

    /**
     * Короткий заголовок статьи
     */
    String getTitleShort();

    /**
     * Личное содержание статьи.
     */
    Toc getToc();

    /**
     * Произвольные свойства статьи
     */
    IVariantMap getProps();

    /**
     * Перезагрузить статью из исходного файла
     */
    void reload();

}
