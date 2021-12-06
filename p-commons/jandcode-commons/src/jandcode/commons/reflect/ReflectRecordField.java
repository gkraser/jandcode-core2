package jandcode.commons.reflect;

import jandcode.commons.named.*;

import java.lang.reflect.*;

/**
 * Интерфейс для поля таблицы, полученной из класса.
 *
 * @see ReflectRecord
 */
public interface ReflectRecordField extends INamed, IReflectProps {

    /**
     * Тип поля
     */
    Class getType();

    /**
     * Метод getter
     */
    Method getGetter();

    /**
     * Метод setter, может быть null
     */
    Method getSetter();

    /**
     * Поле класса, может быть null
     */
    Field getField();

}
