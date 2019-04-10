package jandcode.web.virtfile;

import org.apache.commons.vfs2.*;

import java.io.*;

/**
 * Виртуальный файл для web.
 * <p>
 * Тип файла определяется по расширению. Если в системе зарегистрированы типы файлов
 * шаблоны (например gsp), то:
 * <pre>{@code
 * Реальное
 * имя файла      getName()   isTmlBased()   getFileType()  getContentFileType()
 * ------------   ----------  ------------   -------------  --------------------
 * file.css       file.css    false          css            css
 * file.gsp       file.gsp    false          gsp            gsp
 * file.css.gsp   file.css    true           gsp            css
 * }</pre>
 */
public interface VirtFile {

    /**
     * Полный виртуальный путь файла, включая имя
     */
    String getPath();

    /**
     * Имя файла.
     */
    String getName();

    /**
     * Путь до папки, в которой лежит файл
     */
    String getFolderPath();

    /**
     * Признак папки
     */
    boolean isFolder();

    /**
     * Признак файла
     */
    boolean isFile();

    /**
     * Существует ли
     */
    boolean isExists();

    /**
     * Содержимое файла основано на шаблоне.
     */
    boolean isTmlBased();

    /**
     * Тип файла
     */
    String getFileType();

    /**
     * Тип файла для содержимого.
     */
    String getContentFileType();

    /**
     * Открыть поток для файла.
     * Не забудьте закрыть.
     * Если поток нельзя открыть (например для папок), генерируется ошибка.
     */
    InputStream getInputStream();

    /**
     * Дата последней модификации физического файла.
     * Для несуществующих и папок возвращает 0.
     */
    long getLastModTime();

    /**
     * Приватный файл. Такие файлы не должны быть напрямую доступны клиенту.
     */
    boolean isPrivate();

    /**
     * Размер файла. 0 - для несуществующих или папок.
     */
    long getSize();

    /**
     * Реальный путь файла.
     *
     * @return пустую строку, если реальный путь не известен
     */
    String getRealPath();

    /**
     * Ссылка на реальный FileObject. null, если файл не связан с FileObject.
     */
    FileObject getFileObject();

    /**
     * Загрузить содержимое файла как текст.
     * Если файл нельзя открыть (например для папок), генерируется ошибка.
     * Загружается именно содержимое файла, без всяких предположений о том, что
     * это шаблон.
     */
    String loadText();

}
