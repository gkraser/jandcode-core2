package jandcode.commons.variant;

import jandcode.commons.*;
import jandcode.commons.datetime.*;

/**
 * Типизированное значение по ключу
 */
public interface IVariantIndexed extends IValueIndexed {

    default VariantDataType getDataType(int index) {
        return VariantDataType.fromObject(getValue(index));
    }

    default int getInt(int index) {
        return UtCnv.toInt(getValue(index));
    }

    default long getLong(int index) {
        return UtCnv.toLong(getValue(index));
    }

    default double getDouble(int index) {
        return UtCnv.toDouble(getValue(index));
    }

    default XDateTime getDateTime(int index) {
        return UtCnv.toDateTime(getValue(index));
    }

    default XDate getDate(int index) {
        return UtCnv.toDate(getValue(index));
    }

    default String getString(int index) {
        return UtCnv.toString(getValue(index));
    }

    default boolean getBoolean(int index) {
        return UtCnv.toBoolean(getValue(index));
    }

    /**
     * Возвращает true, если значение null.
     * getValue(index) при этом может возвращать не null.
     */
    default boolean isValueNull(int index) {
        return getValue(index) == null;
    }

    /**
     * Возвращает null, если isValueNull(index)==true, иначе возвращает getValue(index)
     */
    default Object getValueNullable(int index) {
        if (isValueNull(index)) {
            return null;
        }
        return getValue(index);
    }

}
