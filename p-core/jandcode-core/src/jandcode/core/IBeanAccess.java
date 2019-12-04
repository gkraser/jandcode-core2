package jandcode.core;

import java.util.*;

/**
 * Доступ к экземплярам бинов
 */
public interface IBeanAccess {

    /**
     * Получить бин по классу
     *
     * @param cls класс
     */
    <A> A bean(Class<A> cls);

    /**
     * Получить бин по имени
     *
     * @param name имя
     */
    Object bean(String name);

    /**
     * Получить бин по классу. Если он не найден, то создается и регистрируется
     *
     * @param cls класс
     */
    <A> A inst(Class<A> cls);

    /**
     * Бины, реализующие определенный интерфейс.
     * Если бин реализует нужный интерфейс, но к моменту вызова еще не создан,
     * он создается.
     *
     * @param clazz какой интерфейс
     * @return список с объектами. Всегда не null.
     */
    <A extends Object> List<A> impl(Class<A> clazz);

}
