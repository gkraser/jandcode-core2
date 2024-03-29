package jandcode.commons.reflect;

import java.lang.annotation.*;

/**
 * Стандартные свойства, которыми может обладать поле {@link ReflectRecordField}.
 * Аннотация может быть назначена на getter или field.
 * Свойства из класса наследника перекрывают свойства из предка.
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface FieldProps {

    /**
     * Имя словаря, связанного с этим полем.
     */
    String dict() default "";

    /**
     * Размер поля.
     */
    int size() default 0;

    /**
     * До какого знака округлять.
     */
    int scale() default Integer.MAX_VALUE;

}
