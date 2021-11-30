package jandcode.core.dao;

import java.lang.annotation.*;

/**
 * Метка-имя для класса или метода dao.
 * Используется для переименования класса или метода для подсистемы dao.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface DaoName {
    String value();
}
