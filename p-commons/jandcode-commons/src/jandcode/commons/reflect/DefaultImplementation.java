package jandcode.commons.reflect;

import java.lang.annotation.*;

/**
 * Аннотация для интерфейсов с указанием класса реализатора по умолчанию.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface DefaultImplementation {

    /**
     * Класс, который является реализацией по умолчанию
     */
    Class value();

}
