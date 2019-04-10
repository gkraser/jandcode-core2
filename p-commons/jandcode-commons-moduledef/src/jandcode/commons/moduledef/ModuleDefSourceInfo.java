package jandcode.commons.moduledef;

import java.util.*;

/**
 * Представление ModuleDef в исходниках.
 */
public interface ModuleDefSourceInfo {

    /**
     * Ссылка на модуль, для которого это определение
     */
    ModuleDef getModuleDef();

    /**
     * Является ли модулем с исходниками
     */
    boolean isSource();

    /**
     * Путь до проекта модуля. Это каталог, в нем лежит project.jc
     */
    String getProjectPath();

    /**
     * Каталоги с исходниками
     */
    List<String> getSrcPaths();

    /**
     * Каталоги с генерируемыми исходниками
     */
    List<String> getSrcGenPaths();

    /**
     * Каталоги с тестами
     */
    List<String> getTestPaths();

    /**
     * Каталоги с генерируемыми тестами
     */
    List<String> getTestGenPaths();


}
