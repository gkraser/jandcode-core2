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

    default String getString(int index) {
        return UtCnv.toString(getValue(index));
    }

    default boolean getBoolean(int index) {
        return UtCnv.toBoolean(getValue(index));
    }

    default boolean isNull(int index) {
        return getValue(index) == null;
    }

}
