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

    /**
     * Получить значение поля для указанного экземпляра
     *
     * @param inst экземпляр записи
     * @return null, если нет getter
     */
    Object getValue(Object inst);

    /**
     * Установить значение поля для указанного экземпляра.
     * Если нет setter, вызов игнорируется.
     *
     * @param inst  экземпляр записи
     * @param value значение
     */
    void setValue(Object inst, Object value);

}
