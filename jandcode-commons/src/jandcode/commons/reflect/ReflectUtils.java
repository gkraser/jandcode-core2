package jandcode.commons.reflect;

import jandcode.commons.conf.*;

import java.util.*;

/**
 * Утилиты для рефлексии
 */
public interface ReflectUtils {

    /**
     * Получить обертку класса
     *
     * @param cls для какого класса
     */
    ReflectClazz getClazz(Class cls);

    /**
     * Список всех классов из указанного пакета.
     *
     * @param packageName имя пакета java
     * @param recursive   false - только непосредственно в пакете
     */
    List<ReflectClazz> list(String packageName, boolean recursive);

    /**
     * Установить элементы из map свойствам объекта inst через рефлексию
     * Игнорируются атрибуты, который начинаются со знака подчеркивания.
     */
    void setProps(Object inst, Map props);

    /**
     * Возвращает реализацию по умолчанию для указанного интерфейса.
     * Если у класса есть аннотация {@link DefaultImplementation}, то возвращется указанный
     * класс, иначе - cls.
     */
    Class getDefaultImplementation(Class cls);

}
