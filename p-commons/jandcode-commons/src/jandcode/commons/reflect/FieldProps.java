package jandcode.commons.reflect;

import java.lang.annotation.*;

/**
 * Стандартные свойства, которыми может обладать поле {@link ReflectTableField}.
 * Аннотация может быть назначена на getter или field.
 * Свойства из класса наследника перекрывают свойства из предка.
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface FieldProps {

    /**
     * Имя словаря, связанного с этим полем.
     */
    String dict() default "";

}
