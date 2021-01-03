package jandcode.jc;

import java.lang.annotation.*;

/**
 * Опция команды-метода
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Repeatable(CmOptDefs.class)
public @interface CmOptDef {

    /**
     * Имя опции.
     */
    String name() default "";

    /**
     * help для опции
     */
    String help() default "";

    /**
     * Имеется ли аргумент у опции
     */
    boolean hasArg() default false;

}
