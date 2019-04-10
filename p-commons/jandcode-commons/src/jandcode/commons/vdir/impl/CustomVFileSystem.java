package jandcode.commons.vdir.impl;

/**
 * Абстрактная файловая система для обобщения получения информации о
 * файлах File и FileObject
 */
public abstract class CustomVFileSystem {

    /**
     * Сделать из пути абсолютный
     */
    public abstract String abs(String path);

    /**
     * Получить файловый объект
     *
     * @param rootPath     корень
     * @param relativePath относительный путь
     */
    public abstract Object resolveFile(String rootPath, String relativePath);

    /**
     * Существует ли?
     */
    public abstract boolean exists(Object file) throws Exception;

    /**
     * Каталог ли?
     */
    public abstract boolean isDir(Object file) throws Exception;

    /**
     * Список дочерних объектов
     */
    public abstract Object[] listChilds(Object file) throws Exception;

    /**
     * Имя файла без пути
     */
    public abstract String fileName(Object file) throws Exception;

    /**
     * Полное абсолютное имя файла
     */
    public abstract String fullName(Object file) throws Exception;

    /**
     * Относительный путь
     *
     * @param fromPath для какого файла получаем относительный
     * @param toPath   относительно какого файла получаем относительный путь
     */
    public abstract String relativePath(String fromPath, String toPath) throws Exception;


}
