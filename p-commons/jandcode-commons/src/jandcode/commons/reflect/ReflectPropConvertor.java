package jandcode.commons.reflect;

/**
 * Конвертор текстовых значений свойств conf в значения для свойств
 */
public interface ReflectPropConvertor {

    /**
     * Из строки в значение для setter
     *
     * @param s исходная строка
     * @return значение, которое можно передавать в setter
     */
    Object fromString(String s);

}
