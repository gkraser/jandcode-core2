package jandcode.web.virtfile;

import jandcode.commons.io.*;
import org.apache.commons.vfs2.*;

import java.util.*;

/**
 * Интерфейс сервиса для статических файлов web
 */
public interface IVirtFileService extends IVirtFileFind {

    /**
     * Найти файл по виртуальному пути.
     *
     * @param path виртуальный путь
     * @return ошибка, если файл не найден или он папка
     */
    VirtFile getFile(String path);

    /**
     * Получить список файлов в указанном виртуальном каталоге.
     *
     * @return всегда не null, даже если файлов нет.
     */
    List<VirtFile> findFiles(String path);

    /**
     * Создать {@link DirScanner} для виртуального каталога
     *
     * @param dir
     */
    DirScanner<VirtFile> createDirScanner(String dir);

    /**
     * Точки монтирования виртуальной файловой системы.
     * Только для чтения.
     */
    List<Mount> getMounts();

    /**
     * Создать обертку {@link VirtFile} вокруг реального файла
     */
    VirtFile wrapFile(FileObject realFile);

    /**
     * Создать обертку  {@link VirtFile} вокруг реального файла
     */
    VirtFile wrapFile(String realFileName);

    ////// типы файлов

    /**
     * Возвращает тип для указанного расширения. Если расширение не зарегистрировано,
     * возвращает тип "private"
     *
     * @param name имя типа (расширение)
     */
    jandcode.web.virtfile.FileType findFileType(String name);

    /**
     * Список зарегистрированных типов файлов
     */
    List<jandcode.web.virtfile.FileType> getFileTypes();

}
