package jandcode.commons.reflect;

/**
 * Интерфейс для объектов, которым нужно реагировать на присвоение неизвестных
 * рефлексии свойств.
 */
public interface IReflectUnknownSetter {

    /**
     * Вызывается из {@link ReflectClazz#invokeSetter(java.lang.Object, java.lang.String, java.lang.Object)}
     * для неизвестных рефлексии свойств.
     *
     * @param name  имя атрибута
     * @param value значение
     */
    void onUnknownSetter(String name, Object value);

}
