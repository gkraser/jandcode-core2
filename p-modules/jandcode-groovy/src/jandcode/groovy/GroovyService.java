package jandcode.groovy;

import jandcode.commons.groovy.*;
import jandcode.core.*;
import jandcode.core.std.*;

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
