package jandcode.mdoc.source;

import java.io.*;

/**
 * Исходный файл
 */
public interface SourceFile {

    /**
     * Полный путь файла.
     */
    String getPath();

    /**
     * Реальный путь файла.
     * Может быть null, если файл не знает откуда он взялся.
     */
    String getRealPath();

    /**
     * Скопировать в указанный физический файл.
     */
    void copyTo(String destFile) throws Exception;

    /**
     * Скопировать в указанный поток.
     * Автоматически НЕ закрывается!
     */
    void copyTo(OutputStream stm) throws Exception;

    /**
     * Текст файла
     */
    String getText();

    /**
     * Дата последней модификации физического файла.
     * Для виртуальных - время создания виртуального файла.
     */
    long getLastModTime();

}

