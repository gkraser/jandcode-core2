package jandcode.core.web.type;

import jandcode.core.*;

/**
 * Объявление type.
 * Имя компонента - это имя класса, для которого он определен.
 */
public interface TypeDef extends Comp {

    /**
     * Класс, для которого определен компонент.
     * Получен из имени компонента.
     */
    Class getCls();

    /**
     * Получить атрибут, связанный с этим типом.
     *
     * @return null, если атрибут не найден
     */
    String getAttr(String name);

}
