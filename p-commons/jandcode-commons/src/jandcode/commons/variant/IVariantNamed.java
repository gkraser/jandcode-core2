package jandcode.commons.variant;

import jandcode.commons.*;
import jandcode.commons.datetime.*;

/**
 * Типизированное значение по ключу
 */
public interface IVariantNamed extends IValueNamed {

    default VariantDataType getDataType(String name) {
        return VariantDataType.fromObject(getValue(name));
    }

    default int getInt(String name) {
        return UtCnv.toInt(getValue(name));
    }

    default long getLong(String name) {
        return UtCnv.toLong(getValue(name));
    }

    default double getDouble(String name) {
        return UtCnv.toDouble(getValue(name));
    }

    default XDateTime getDateTime(String name) {
        return UtCnv.toDateTime(getValue(name));
    }

    default String getString(String name) {
        return UtCnv.toString(getValue(name));
    }

    default boolean getBoolean(String name) {
        return UtCnv.toBoolean(getValue(name));
    }

    default boolean isNull(String name) {
        return getValue(name) == null;
    }

}
