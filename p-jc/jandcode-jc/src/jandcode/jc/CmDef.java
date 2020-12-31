package jandcode.jc;

import java.lang.annotation.*;

/**
 * Пометка метода, как команды.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface CmDef {

    /**
     * Имя команды, если не устраивает имя, определенное по имени метода.
     */
    String name() default "";

    /**
     * help
     */
    String help() default "";

}
