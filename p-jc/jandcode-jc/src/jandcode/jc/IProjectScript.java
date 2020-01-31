package jandcode.jc;

import groovy.ant.*;
import jandcode.jc.std.*;

/**
 * Скрипт, который можно включить в проект.
 * vars у скрипта собственная, остальные методы из интерфейса Project
 * делегируются проекту.
 */
public interface IProjectScript extends Project, ILog, IClasspath {

    /**
     * Проект, для которого выполняется скрипт
     */
    Project getProject();

    /**
     * Map с набором переменных скрипта.
     * Можно свободно писать и читать.
     * Скрипт и проект имеют собственные vars.
     */
    Vars getVars();

    /**
     * Прерывание работы скрипта с генерацией ошибки
     */
    void error(Object msg);

    /**
     * Загрузить проект
     *
     * @param projectPath из какого файла
     *                    (см. {@link IProjects#resolveProjectFile(java.lang.String, java.lang.String)},
     *                    где basePath - каталог проекта)
     */
    Project load(String projectPath);

    /**
     * Загрузить проект
     *
     * @param projectPath из какого файла
     *                    (см. {@link IProjects#resolveProjectFile(java.lang.String, java.lang.String)},
     *                    где basePath - каталог проекта)
     * @param require     должен ли быть проект. Если false и проект не существует,
     *                    то метод возвращает null
     */
    Project load(String projectPath, boolean require);


    /**
     * ant, настроенный на рабочий каталог проекта
     */
    AntBuilder getAnt();

    /**
     * Набор всяких утилит
     */
    Ut getUt();

    /**
     * Вовращает каталог, в котором находится скрипт.
     * Если для скрипта нельзя определить каталог, то возвращается каталог проекта.
     */
    String getScriptDir();

}
