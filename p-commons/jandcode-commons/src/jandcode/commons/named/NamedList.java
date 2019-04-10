package jandcode.commons.named;

import java.util.*;

/**
 * Список поименнованных объектов
 */
public interface NamedList<TYPE extends INamed> extends List<TYPE> {

    /**
     * Найти по имени
     *
     * @return null, если не найден
     */
    TYPE find(String name);

    /**
     * Найти по имени
     *
     * @return ошибка, если не найден
     */
    TYPE get(String name);

    /**
     * Сортировка по имени
     */
    void sort();

    /**
     * Список имен, которые используются в этом списке
     */
    List<String> getNames();

    //////

    /**
     * Установить объект для отображения сообщения об не найденном элементе.
     * <p>
     * msg может бытт строкой, тогда в ней должен быть '{0}', вместо которого подставится
     * имя не найденного объекта.
     * <p>
     * msg может быть экземпляром {@link NamedNotFoundMessage}.
     * <p>
     * msg может быть null, тогда будет использоваться сообщение по умолчанию.
     *
     * @param msg    сообщение.
     * @param params дополнительные параметры для сообщения об ошибке, подставляются
     *               вместо {1} и т.д. Если параметр INamed, то берется его имя,
     *               иначе - toString()
     */
    void setNotFoundMessage(Object msg, Object... params);

    /**
     * см: {@link NamedList#setNotFoundMessage(java.lang.Object, java.lang.Object...)},
     * где params=null
     */
    void setNotFoundMessage(Object msg);

}
