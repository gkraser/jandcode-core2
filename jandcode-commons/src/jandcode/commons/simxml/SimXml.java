package jandcode.commons.simxml;

import jandcode.commons.io.*;
import jandcode.commons.named.*;
import jandcode.commons.variant.*;

import java.util.*;

/**
 * Представление узла xml.
 */
public interface SimXml extends INamed, INamedSet, IVariantNamed, IVariantNamedDefault,
        IValueNamedSet {

    /**
     * Имеется ли установленное имя
     */
    boolean hasName();

    /**
     * Проверка на совпадение имени.
     */
    boolean hasName(String name);


    ////// text

    /**
     * Текст узла
     */
    String getText();

    /**
     * Установить текст узла
     */
    void setText(String text);

    /**
     * Есть ли текст у узла. Если текст - пустая строка - то текста нет!
     */
    boolean hasText();


    ////// attrs

    /**
     * Есть ли атрибуты
     */
    boolean hasAttrs();

    /**
     * Очистить атрибуты
     */
    void clearAttrs();

    /**
     * Атрибуты
     */
    IVariantMap getAttrs();


    ////// childs

    /**
     * Есть ли дочерние
     */
    boolean hasChilds();

    /**
     * Очистить все дочерние
     */
    void clearChilds();

    /**
     * Итератор по дочерним узлам
     */
    List<SimXml> getChilds();


    ////// add child

    /**
     * Добавить дочерний узел
     */
    void addChild(SimXml child);

    /**
     * Создать и добавить дочерний узел с указанным именем
     */
    SimXml addChild(String name);


    ////// remove child

    /**
     * Удалить дочерний, если он есть
     */
    void removeChild(SimXml x);

    /**
     * Удалить дочерний по индексу
     */
    void removeChild(int index);


    ////// find

    /**
     * Поиск дочернего по пути
     *
     * @param path путь
     * @return узел или null, если не найден
     */
    default SimXml findChild(String path) {
        return findChild(path, false);
    }

    /**
     * Поиск дочернего по пути
     *
     * @param path             путь
     * @param createIfNotExist true - создавать не существующие
     * @return узел или null, если не найден
     */
    SimXml findChild(String path, boolean createIfNotExist);


    ////// misc

    /**
     * Очистка всех данных, кроме имени.
     */
    void clear();


    ////// io

    /**
     * Создание загрузчика
     */
    LoadFrom load();

    /**
     * Создание записывальщика
     */
    SaveTo save();


    ////// for groovy

    /**
     * Аналогично {@link SimXml#getValue(String)}
     */
    default Object getAt(String path) {
        return getValue(path);
    }

    /**
     * Аналогично {@link SimXml#setValue(String, Object)}
     */
    default void putAt(String path, Object value) {
        setValue(path, value);
    }

}
