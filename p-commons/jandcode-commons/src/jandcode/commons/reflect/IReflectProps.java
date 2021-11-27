package jandcode.commons.reflect;

import java.util.*;

/**
 * Произвольный набор свойств, которыми может обладать
 * объект.
 */
public interface IReflectProps {

    /**
     * Установить значение произвольного свойства.
     *
     * @param name  имя свойства
     * @param value значение свойства. При значении null - свойство удаляется
     */
    void setProp(String name, Object value);

    /**
     * Получить значение произвольного свойства.
     * Если свойства нет, возвращается null.
     *
     * @param name имя свойства
     * @return null, если свойства нет
     */
    Object getProp(String name);

    /**
     * Все имена произвольных свойств
     */
    Collection<String> getPropNames();

}
