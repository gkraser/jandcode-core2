package jandcode.commons.variant;

import java.util.*;

/**
 * Владелец полей.
 * Может предоставить абстрактную информацию о своих полях.
 * Значимость регистра имени поля определяется владельцем.
 */
public interface IVariantFieldsOwner {

    /**
     * Имеется ли поле с указанным именем.
     * Значимость регистра имени определяется владельцем.
     *
     * @param fieldName имя поля
     * @return true, если есть такое поле.
     */
    boolean hasField(String fieldName);

    /**
     * Имена полей.
     * Значимость регистра имени определяется владельцем.
     */
    Collection<String> getFieldNames();

}
