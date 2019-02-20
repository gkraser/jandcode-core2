package jandcode.commons.groovy.impl;

import jandcode.commons.*;
import jandcode.commons.error.*;
import jandcode.commons.groovy.*;

import java.util.*;
import java.util.concurrent.*;

/**
 * Реализация для UtGroovy
 */
public class GroovyManager {

    private static GroovyManager inst;

    static {
        inst = new GroovyManager();
        // groovy конвертор ошибок
        UtError.addErrorConvertor(new ErrorConvertorGroovy(inst));
    }

    public static GroovyManager getInst() {
        return inst;
    }

    //////

    private Map<GroovyCompilerImpl, String> compilers = new ConcurrentHashMap<>();

    protected Collection<GroovyCompilerImpl> getCompilers() {
        return compilers.keySet();
    }

    public GroovyCompiler createCompiler() {
        GroovyCompilerImpl res = new GroovyCompilerImpl();
        compilers.put(res, "1");
        return res;
    }

    public void destroyCompiler(GroovyCompiler compiler) {
        GroovyCompilerImpl c = (GroovyCompilerImpl) compiler;
        compilers.remove(c);
        c.destory();
    }


    /**
     * Возвращает {@link ErrorSource} для указанной строки
     * describer, где возможно есть упоминание имени класса.
     * Используется при поиске ошибок компиляции.
     *
     * @param describer строка, которая содержит имя класса
     * @param line      для какой строки
     * @return Если не найдено, возвращается null
     */
    public ErrorSource getErrorSource(String describer, int line) {
        for (GroovyCompilerImpl compiler : getCompilers()) {
            ErrorSource a = compiler.getErrorSource(describer, line);
            if (a != null) {
                return a;
            }
        }
        return null;
    }

    /**
     * Возвращает {@link ErrorSource} для указанного элемента стека.
     *
     * @param st для какого элемента стека
     * @return Если StackTraceElement не связан с исходником скриптов, возвращается null.
     */
    public ErrorSource getErrorSource(StackTraceElement st) {
        for (GroovyCompilerImpl compiler : getCompilers()) {
            ErrorSource a = compiler.getErrorSource(st);
            if (a != null) {
                return a;
            }
        }
        return null;
    }

    /**
     * Запустить проверку изменных исходников скриптов
     */
    public void checkChangedResource() {
        for (GroovyCompilerImpl compiler : getCompilers()) {
            compiler.checkChangedResource();
        }
    }

    public GroovyClazz findClazz(Class cls) {
        for (GroovyCompilerImpl compiler : getCompilers()) {
            GroovyClazz res = compiler.findClazz(cls);
            if (res != null) {
                return res;
            }
        }
        return null;
    }

}
