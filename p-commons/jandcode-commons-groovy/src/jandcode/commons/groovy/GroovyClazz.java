package jandcode.commons.groovy;

import org.apache.commons.vfs2.*;

/**
 * Класс groovy, полученный из исходника
 */
public interface GroovyClazz {

    /**
     * Скомпилированный класс
     */
    Class getClazz();

    /**
     * Создать экземпляр объекта
     */
    Object createInst();

    /**
     * Оригинальный исходник
     */
    String getSourceOriginal();

    /**
     * Исходник скомпилированного класса
     */
    String getSourceClazz();

    /**
     * Файл скрипта. Может быть null, если скрипт получен из текста.
     */
    FileObject getSourceFile();

    /**
     * Компилятор, с помощью которого был создан класс.
     */
    GroovyCompiler getCompiler();
}
