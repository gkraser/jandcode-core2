package jandcode.commons.variant;

import jandcode.commons.*;
import jandcode.commons.datetime.*;

/**
 * Типы данных variant.
 */
public enum VariantDataType {

    OBJECT,
    INT,
    LONG,
    DOUBLE,
    DATETIME,
    STRING,
    BOOLEAN,
    BLOB;

    /**
     * Конвертация из строки. Возвращает OBJECT, если тип неизвестный.
     */
    public static VariantDataType fromString(String s) {
        if (s == null || s.length() == 0) {
            return OBJECT;
        }
        try {
            return valueOf(s.toUpperCase());
        } catch (Exception e) {
            return OBJECT;
        }
    }

    /**
     * Тип данных для класса. Возвращает OBJECT, если класс неизвестный.
     */
    public static VariantDataType fromClass(Class cls) {
        if (cls == null) {
            return OBJECT;

        } else if (cls == int.class) {
            return INT;

        } else if (cls == double.class) {
            return DOUBLE;

        } else if (cls == long.class) {
            return LONG;

        } else if (cls == boolean.class) {
            return BOOLEAN;

        } else if (cls == String.class) {
            return STRING;

        } else if (cls == Integer.class) {
            return INT;

        } else if (cls == Long.class) {
            return LONG;

        } else if (cls == Byte.class) {
            return INT;

        } else if (cls == Short.class) {
            return INT;

        } else if (cls == Double.class) {
            return DOUBLE;

        } else if (cls == Float.class) {
            return DOUBLE;

        } else if (cls == XDateTime.class) {
            return DATETIME;

        } else if (cls == byte[].class) {
            return BLOB;

        } else if (cls == Boolean.class) {
            return BOOLEAN;

        } else if (CharSequence.class.isAssignableFrom(cls)) {
            return STRING;

        } else {
            return OBJECT;
        }
    }

    /**
     * Тип данных для объекта. Возвращает OBJECT, если класс неизвестный.
     */
    public static VariantDataType fromObject(Object inst) {
        if (inst == null) {
            return OBJECT;
        }
        return fromClass(inst.getClass());
    }

    /**
     * true, если тип данных - число
     */
    public static boolean isNumber(VariantDataType dt) {
        return dt != null && (dt == INT || dt == LONG || dt == DOUBLE);
    }


    public String toString() {
        return super.toString().toLowerCase();
    }

    /**
     * Перевести объект value в значение типа VariantDataType
     */
    public static Object toDataType(Object value, VariantDataType dataType) {
        switch (dataType) {
            case INT:
                return UtCnv.toInt(value);
            case LONG:
                return UtCnv.toLong(value);
            case DOUBLE:
                return UtCnv.toDouble(value);
            case DATETIME:
                return UtCnv.toDateTime(value);
            case BOOLEAN:
                return UtCnv.toBoolean(value);
            case STRING:
                return UtCnv.toString(value);
            case BLOB:
                return UtCnv.toByteArray(value);
            case OBJECT:
                return value;
            default:
                return null;
        }
    }

}
