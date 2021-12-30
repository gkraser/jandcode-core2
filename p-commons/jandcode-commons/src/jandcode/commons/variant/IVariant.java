package jandcode.commons.variant;

import jandcode.commons.*;
import jandcode.commons.datetime.*;

/**
 * Типизированное значение
 */
public interface IVariant extends IValue {

    default VariantDataType getDataType() {
        return VariantDataType.fromObject(getValue());
    }

    default int getInt() {
        return UtCnv.toInt(getValue());
    }

    default long getLong() {
        return UtCnv.toLong(getValue());
    }

    default double getDouble() {
        return UtCnv.toDouble(getValue());
    }

    default XDateTime getDateTime() {
        return UtCnv.toDateTime(getValue());
    }

    default XDate getDate() {
        return UtCnv.toDate(getValue());
    }

    default String getString() {
        return UtCnv.toString(getValue());
    }

    default boolean getBoolean() {
        return UtCnv.toBoolean(getValue());
    }

    /**
     * Возвращает true, если значение null.
     * getValue() при этом может возвращать не null.
     */
    default boolean isValueNull() {
        return getValue() == null;
    }

    /**
     * Возвращает null, если isValueNull()==true, иначе возвращает getValue()
     */
    default Object getValueNullable() {
        if (isValueNull()) {
            return null;
        }
        return getValue();
    }

}
