package jandcode.jc;

import jandcode.commons.named.*;

import java.util.*;

/**
 * Библиотека java
 */
public interface Lib extends INamed {

    /**
     * Имя библиотеки.
     * Под этим именем библиотека известна для jandcode.
     */
    String getName();

    /**
     * Версия библиотеки
     */
    String getVersion();

    /**
     * Зависимости библиотеки.
     */
    LibDepends getDepends();

    /**
     * Проект, который является исходником для библиотеки.
     * Если значение не null, значит библиотека доступна в виде проекта,
     * ее можно перекомпилировать, использовать в IDE как модуль и т.д.
     */
    Project getSourceProject();

    //////

    /**
     * Путь для включения в classpath.
     * Гарантируется, что будет существовать после вызова.
     * Например библиотека в исходниках будет перекомпилирована,
     * если она не скомпилирована на момент вызова.
     */
    String getClasspath();

    /**
     * Путь для включения в classpath.
     * Не гарантируется, что будет существовать после вызова.
     */
    String getClasspathRaw();

    /**
     * Путь до jar. Может не совпадать с classpath!
     */
    String getJar();

    /**
     * Путь до исходников либы. Может отсутствовать.
     */
    String getSrc();

    //////

    /**
     * Признак системной библиотеки (например jdk-tools)
     */
    boolean isSys();

    //////

    /**
     * Список имен модулей, которые имеются в этой библиотеке
     */
    List<String> getModules();

    //////

    /**
     * maven: artifactId
     */
    String getArtifactId();

    /**
     * maven: groupId
     */
    String getGroupId();

}
