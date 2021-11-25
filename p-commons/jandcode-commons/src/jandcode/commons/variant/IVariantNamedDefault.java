package jandcode.commons.variant;

import jandcode.commons.*;
import jandcode.commons.datetime.*;

/**
 * Типизированное значение по ключу со значением по умолчанию.
 */
public interface IVariantNamedDefault extends IValueNamed {

    default int getInt(String name, int defValue) {
        return UtCnv.toInt(getValue(name), defValue);
    }

    default long getLong(String name, long defValue) {
        return UtCnv.toLong(getValue(name), defValue);
    }

    default double getDouble(String name, double defValue) {
        return UtCnv.toDouble(getValue(name), defValue);
    }

    default XDateTime getDateTime(String name, XDateTime defValue) {
        return UtCnv.toDateTime(getValue(name), defValue);
    }

    default XDate getDate(String name, XDate defValue) {
        return UtCnv.toDate(getValue(name), defValue);
    }

    default String getString(String name, String defValue) {
        return UtCnv.toString(getValue(name), defValue);
    }

    default boolean getBoolean(String name, boolean defValue) {
        return UtCnv.toBoolean(getValue(name), defValue);
    }

}
