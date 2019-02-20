package jandcode.jc;

import jandcode.commons.named.*;

import java.util.*;

/**
 * Поименнованная группа зависимостей
 */
public interface LibDependsGroup extends INamed {

    /**
     * Имена библиотек в группе. Может включать и имена модулей.
     */
    List<String> getNames();

    /**
     * Библиотеки в группе.
     * Соответствует библиотекам из {@link LibDependsGroup#getNames()},
     * без раскрытия зависимостей.
     */
    ListLib getLibs();

    /**
     * Библиотеки в группе. Раскрытые.
     */
    ListLib getLibsAll();

    /**
     * Модули в группе.
     * Соответствует модулям из {@link LibDependsGroup#getNames()},
     * без раскрытия зависимостей.
     */
    List<String> getModules();

    /**
     * Добавить библиотеки в набор
     */
    void add(Object... libNames);

    /**
     * Удалить библиотеки из набора
     */
    void remove(Object libNames);

    /**
     * Возвращает true, если в группу включена хотя бы одна из запрошенных
     * библиотек
     */
    boolean contains(Object libNames);

    /**
     * Возвращает true, если группа пустая
     */
    boolean isEmpty();

}
