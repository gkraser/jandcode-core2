package jandcode.core;

import java.util.*;

/**
 * Произвольный набор свойств, которыми может обладать
 * компонент.
 */
public interface ICustomProp {

    /**
     * Установить значение произвольного свойства.
     *
     * @param name  имя свойства
     * @param value значение свойства. При значении null - свойство удаляется
     */
    void setCustomProp(String name, Object value);

    /**
     * Получить значение произвольного свойства.
     * Если свойства нет, возвращается null.
     *
     * @param name имя свойства
     * @return null, если свойства нет
     */
    Object getCustomProp(String name);

    /**
     * Все имена произвольных свойств
     */
    Collection<String> getCustomPropNames();

}
