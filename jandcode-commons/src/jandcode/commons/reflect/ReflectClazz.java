package jandcode.commons.reflect;

/**
 * Интерфейс к обертке класса в {@link ReflectUtils}
 */
public interface ReflectClazz {

    /**
     * Реальный класс
     */
    Class getCls();

    /**
     * Установить свойство объекту.
     * Генерится ошибка, если произошла при присвоении/
     *
     * @param inst     экземпляр
     * @param propname имя свойства. Определяется методами inst:
     *                 setPropname(Object), setPropnameName(String).
     *                 Если таких методов нет, но экхемпляр реализует интерфейс
     *                 {@link IReflectUnknownSetter}, то вызывается метод
     *                 {@link IReflectUnknownSetter#onUnknownSetter(java.lang.String, java.lang.Object)}.
     * @param value    значение
     */
    void invokeSetter(Object inst, String propname, Object value);

    /**
     * Получить свойство объекта
     *
     * @param inst     экземпляр
     * @param propname имя свойства. Определяется методами inst:
     *                 getPropname()
     * @return значение свойства
     */
    Object invokeGetter(Object inst, String propname);

}
