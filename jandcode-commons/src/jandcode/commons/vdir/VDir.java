package jandcode.commons.vdir;

import java.util.*;

/**
 * Виртуальный каталог. Собирается из нескольких реальных каталогов,
 * которые используются как корни и обеспечивает прозрачный доступ так,
 * как будно все файлы лежат в одном месте. По запросам возвращает
 * объекты {@link VFile}.
 */
public interface VDir {

    /**
     * Информация о все корнях в порядке приоритета
     */
    List<VRoot> getRoots();

    /**
     * Добавить корень
     */
    void addRoot(String path);

    /**
     * Добавить корень с указанным префиксом пути
     */
    void addRoot(String path, String prefixPath);

    /**
     * Найти список дочерних файлов (и каталогов) в указанном пути
     *
     * @param path виртуальный путь
     */
    List<VFile> findFiles(String path);

    /**
     * Найти файл (или каталог)
     *
     * @param path виртуальный путь
     * @return null, если файл не найден
     */
    VFile findFile(String path);

    /**
     * Все реальные пути. Используется для объектов, которые присутствуют
     * в нескольких реальных путях.
     */
    List<String> getRealPathList(String path);

    /**
     * Все файлы с указанным путем в виртуальном каталоге
     */
    List<VFile> getFileEntry(String virtualPath);

    /**
     * Получить виртуальный путь для реального. Возвращает null, если абсолютный путь
     * не из этого виртуального каталога.
     */
    String getVirtualPath(String absolutePath);

}
