package jandcode.jc;

import java.util.*;

/**
 * Проекты
 */
public interface IProjects {

    /**
     * Загрузить проект
     *
     * @param projectPath из какого файла
     *                    (см. {@link IProjects#resolveProjectFile(java.lang.String, java.lang.String)},
     *                    где basePath - текущий рабочий каталог)
     */
    Project load(String projectPath);

    /**
     * Все загруженные проекты
     */
    Collection<Project> getProjects();

    /**
     * Найти физический файл проекта по абстрактному имени.
     *
     * @param basePath    базовый каталог, откуда нужно искать
     * @param projectPath путь до файла проекта. Может быть относительным или абсолютным
     *                    путем. Если путь указывает на каталог, то в каталоге должен
     *                    быть файл 'project.jc'. Если путь указывает на файл,
     *                    то расширение '.jc' можно опускать.
     *                    Если расширение не указано и имеется и каталог и файл
     *                    с таком именем, то преимущество имеет файл.
     *                    Например {@code load('../pro1')}
     *                    при наличии каталога '../pro1' и файла '../pro1.jc',
     *                    загрузит файл '../pro1.jc'.
     * @return полное имя файла проекта или null, если файл проекта не найден
     */
    String resolveProjectFile(String basePath, String projectPath);

    /**
     * Для указанного каталога возвращает его корневой проект, т.е. такой,
     * который содержит файл .jc-root
     *
     * @param path для какого пути
     * @return null, если нет корневого проекта для указанного пути
     */
    Project getRootProject(String path);

}
