package jandcode.core.dao;

import java.lang.annotation.*;

/**
 * Метка для метода dao. Все остальные методы, не помеченные этой аннотацией,
 * являются просто методами класса
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Deprecated
public @interface DaoMethod {
}
