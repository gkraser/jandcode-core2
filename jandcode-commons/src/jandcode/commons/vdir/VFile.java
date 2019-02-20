package jandcode.commons.vdir;

import java.util.*;

/**
 * Представление файла/каталога в виртуальном каталоге.
 * toString() - реальный путь
 */
public interface VFile {

    /**
     * Виртуальный путь
     */
    String getVirtualPath();

    /**
     * Реальный путь
     */
    String getRealPath();

    /**
     * Все реальные пути. Используется для объектов, которые присутствуют
     * в нескольких реальных путях.
     */
    List<String> getRealPathList();

    /**
     * Имя файла без пути
     */
    String getFileName();

    /**
     * Расширение файла
     */
    String getExt();

    /**
     * Является ли каталогом
     */
    boolean isDir();

    /**
     * Является ли файлом
     */
    boolean isFile();

    /**
     * Индекс файла в виртуальном каталоге. Чем меньше индекс, тем выше приоритет
     */
    int getIndex();

}
