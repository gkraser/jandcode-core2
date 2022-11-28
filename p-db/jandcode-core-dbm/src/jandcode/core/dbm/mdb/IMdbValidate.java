package jandcode.core.dbm.mdb;

import java.util.*;

/**
 * Методы для валидации
 */
public interface IMdbValidate {

    /**
     * Проверить данные указанным валидатором
     *
     * @param data          что проверяем
     * @param validatorName имя валидатора
     * @param attrs         атрибуты для контекста валидации
     * @return false, если были ошибки валидации
     */
    boolean validate(Object data, String validatorName, Map attrs) throws Exception;

    /**
     * Проверить данные указанным валидатором
     *
     * @param data          что проверяем
     * @param validatorName имя валидатора
     * @return false, если были ошибки валидации
     */
    default boolean validate(Object data, String validatorName) throws Exception {
        return validate(data, validatorName, null);
    }

    /**
     * Проверить данные валидатором 'record'
     *
     * @param data  что проверяем
     * @param attrs атрибуты для контекста валидации
     * @return false, если были ошибки валидации
     */
    boolean validateRecord(Object data, Map attrs) throws Exception;

    /**
     * Проверить данные валидатором 'record'
     *
     * @param data что проверяем
     * @return false, если были ошибки валидации
     */
    default boolean validateRecord(Object data) throws Exception {
        return validateRecord(data, null);
    }

    /**
     * Проверить данные валидатором 'field'
     *
     * @param data      что проверяем
     * @param fieldName какое поле проверяем
     * @param attrs     атрибуты для контекста валидации
     * @return false, если были ошибки валидации
     */
    boolean validateField(Object data, String fieldName, Map attrs) throws Exception;

    /**
     * Проверить данные валидатором 'field'
     *
     * @param data      что проверяем
     * @param fieldName какое поле проверяем
     * @return false, если были ошибки валидации
     */
    default boolean validateField(Object data, String fieldName) throws Exception {
        return validateField(data, fieldName, null);
    }

}
