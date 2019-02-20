package jandcode.commons.reflect;

import java.lang.annotation.*;

/**
 * Аннотация для пометки setter, как инициализируемого из конфигурации conf
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface ReflectProp {

    /**
     * Имя атрибута
     */
    String name() default "";

    /**
     * Конвертор значений атрибута. Значение ReflectProp.class считается как null!
     */
    Class convertor() default ReflectProp.class;

}