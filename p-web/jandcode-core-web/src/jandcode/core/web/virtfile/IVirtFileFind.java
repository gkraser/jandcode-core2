package jandcode.core.web.virtfile;

/**
 * Поиск виртуального файла
 */
public interface IVirtFileFind {

    /**
     * Найти файл по виртуальному пути.
     *
     * @param path виртуальный путь. Если пустая строка - возвращается null.
     * @return null, если файл не найден
     */
    VirtFile findFile(String path);


}
