package jandcode.commons.groovy;

import groovy.lang.*;
import jandcode.commons.error.*;

import java.util.*;

/**
 * Интерфейс для классов, являющихся gsp шаблонами
 */
public interface IGspTemplate {

    /**
     * Вывести текст
     */
    void out(Object s) throws Exception;

    /**
     * Вывести тег
     */
    default void outTag(String name, Map args, Closure cls) throws Exception {
        throw new XError("Unsupported method outTag(name, args, closure)");
    }

    /**
     * Вывести тег
     */
    default void outTag(Map args, String name, Closure cls) throws Exception {
        outTag(name, args, cls);
    }

    /**
     * Вывести тег
     */
    default void outTag(Map args, String name) throws Exception {
        outTag(name, args, null);
    }

    /**
     * Вывести тег
     */
    default void outTag(String name) throws Exception {
        outTag(name, null, null);
    }

    /**
     * Вывести тег
     */
    default void outTag(String name, Map args) throws Exception {
        outTag(name, args, null);
    }

    /**
     * Вывести тег
     */
    default void outTag(String name, Closure cls) throws Exception {
        outTag(name, null, cls);
    }

}
