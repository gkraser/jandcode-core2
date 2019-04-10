package jandcode.mdoc.source;

import java.util.*;

/**
 * Набор исходных файлов.
 */
public interface SourceFileHolder extends Iterable<SourceFile> {

    /**
     * Добавить файл. Если файл с таким {@link SourceFile#getPath()} уже
     * существует, он заменяется на новый.
     */
    void add(SourceFile f);

    /**
     * Коллекция всех файлов
     */
    Collection<SourceFile> getItems();

    /**
     * Коллекция файлов, удовлетворяющих маске
     */
    Collection<SourceFile> findFiles(String mask);

    /**
     * Найти файл по виртуальному пути.
     *
     * @return null, если не найден
     */
    SourceFile find(String virtualPath);

    /**
     * Найти файл по виртуальному пути
     *
     * @return ошибка, если не найден
     */
    SourceFile get(String virtualPath);

}
