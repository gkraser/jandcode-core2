package jandcode.jc;

import java.util.*;

/**
 * Библиотеки
 */
public interface ILibs extends IClasspath {

    /**
     * Добавить провайдер библиотек
     */
    void addLibProvider(ILibProvider p);

    /**
     * Получить список провайдеров библиотек.
     * Самый первый имеет наивысший приоритет.
     */
    List<ILibProvider> getLibsProviders();

    /**
     * Добавить каталог с библиотеками
     */
    void addLibDir(String path);

    /**
     * Библиотека по имени.
     * В качестве имени можно использовать как имя библиотеки, так и имя модуля.
     *
     * @return null, если не найдена
     */
    Lib findLib(String name);

    /**
     * Библиотека по имени.
     * В качестве имени можно использовать как имя библиотеки, так и имя модуля.
     */
    Lib getLib(String name);

    /**
     * Возвращает список, состоящий из библиотек names и всех их зависимостей.
     *
     * @param libNames   имена библиотек. Может быть списком или объектом. Если объект, тогда:
     *                   строка - имя библиотеки, INamed - getName() имя библиотеки.
     * @param groupNames имена групп зависимостей, которые участвуют в раскрытии.
     *                   если не указано (null или пустая строка),
     *                   то подразумевается "prod"
     */
    ListLib getLibs(Object libNames, String groupNames);

    /**
     * Возвращает список, состоящий из библиотек names и всех их зависимостей.
     * см: {@link ILibs#getLibs(java.lang.Object, java.lang.String)}
     * с группой "prod".
     */
    ListLib getLibs(Object libNames);

    /**
     * Все доступные библиотеки
     */
    ListLib getLibs();

    /**
     * Загружает библиотеки из указанного каталога в стандарте lib-каталога.
     * Загруженные библиотеки являются копиями и не пересекаются с текущими
     * библиотеками, доступными в контекте.
     *
     * @param path из какого каталога загружать
     * @return список библиотек
     */
    ListLib loadLibs(String path);

    /**
     * Найти библиотеку, в которой объявлен модуль
     *
     * @param moduleName имя модуля
     * @return null, если не найден
     */
    Lib findLibForModule(String moduleName);

}
