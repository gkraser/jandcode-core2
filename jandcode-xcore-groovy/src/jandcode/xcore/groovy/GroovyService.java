package jandcode.xcore.groovy;

import jandcode.commons.groovy.*;
import jandcode.xcore.*;
import jandcode.xcore.std.*;

import java.util.*;

/**
 * Сервис для groovy.
 * В частности хранит набор компиляторов, которые нужны объектам приложения.
 */
public interface GroovyService extends Comp, ICheckChangedResource {

    /**
     * Возвращает указанный компилятор по имени.
     * Если он не существует, создает новый.
     */
    GroovyCompiler getGroovyCompiler(String name);

    /**
     * Возвращает список имен компиляторов, которые зарегистрированы к моменту
     * вызова метода.
     */
    List<String> getGroovyCompilerNames();

}
