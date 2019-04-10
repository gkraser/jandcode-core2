package jandcode.core;

import jandcode.commons.conf.*;
import jandcode.commons.named.*;

/**
 * Фабрика бинов
 */
public interface IBeanFactory {

    /**
     * Создание и инициализация объекта.
     * Процесс создания:
     * <ul>
     * <li>Определяется класс для экземпляра. Порядок определения:
     * <ul>
     * <li>Если findClassInConf=true, есть conf и в ней есть атрибут class, то берется он</li>
     * <li>Значение параметра cls</li>
     * </ul>
     * </li>
     * <li>Создается экземпляр объекта</li>
     * <li>Фабрика инициализирует объект по своему усмотрению, в зависимости
     * от его экземпляра, класса и conf (если есть)</li>
     * <li>Если указан initer, то он вызывается для экземпляра. Это позволяет
     * настроить объект перед загрузкой conf</li>
     * <li>Если есть conf:
     * <ul>
     * <li>Если объект не реализует интерфейс {@link IBeanConfigure},
     * то, если объект реализует интерфейс {@link INamedSet} и имя ему еще не назначено,
     * то ему присваивается имя conf.
     * После этого объект инициализируется свойствами из conf</li>
     * </ul>
     * </li>
     * <li>Если объект реализует интерфейс {@link IBeanConfigure}, то вызывается
     * конфигурирование объекта.
     * </li>
     * <li>Объект готов к использованию</li>
     * </ul>
     *
     * @param cls             класс.
     *                        Если не указан, и имеется conf, то выбирается атрибут class из conf.
     *                        Если указан, то используется если нет class в conf.
     *                        Если и там ничего нет - ошибка.
     *                        Т.е. при наличии conf - это класс по умолчанию.
     * @param cfg             конфиг для объекта. Может быть null
     * @param findClassInConf true - искать класс в conf, если не найден - использовать cls.
     *                        false - использовать cls
     * @param initer          инициализатор объекта
     */
    <A> A create(Class<A> cls, BeanConfig cfg, boolean findClassInConf, IBeanIniter initer);

    /**
     * @see IBeanFactory#create(java.lang.Class, jandcode.core.BeanConfig, boolean, jandcode.core.IBeanIniter)
     */
    <A> A create(Class<A> cls, Conf conf, boolean findClassInConf, IBeanIniter initer);

    /**
     * @see IBeanFactory#create(Class, Conf, boolean, IBeanIniter)
     */
    <A> A create(Conf conf, Class<A> cls, boolean findClassInConf, IBeanIniter initer);

    /**
     * @see IBeanFactory#create(Class, BeanConfig, boolean, IBeanIniter)
     */
    <A> A create(BeanConfig cfg, Class<A> cls, boolean findClassInConf, IBeanIniter initer);

    /**
     * @see IBeanFactory#create(Class, Conf, boolean, IBeanIniter)
     */
    <A> A create(Conf conf, Class<A> cls, IBeanIniter initer);

    /**
     * @see IBeanFactory#create(Class, BeanConfig, boolean, IBeanIniter)
     */
    <A> A create(BeanConfig cfg, Class<A> cls, IBeanIniter initer);

    /**
     * @see IBeanFactory#create(Class, Conf, boolean, IBeanIniter)
     */
    <A> A create(Conf conf, Class<A> cls, boolean findClassInConf);

    /**
     * @see IBeanFactory#create(Class, BeanConfig, boolean, IBeanIniter)
     */
    <A> A create(BeanConfig cfg, Class<A> cls, boolean findClassInConf);

    /**
     * @see IBeanFactory#create(Class, Conf, boolean, IBeanIniter)
     */
    <A> A create(Conf conf, Class<A> cls);

    /**
     * @see IBeanFactory#create(Class, BeanConfig, boolean, IBeanIniter)
     */
    <A> A create(BeanConfig cfg, Class<A> cls);

    /**
     * @see IBeanFactory#create(Class, Conf, boolean, IBeanIniter)
     */
    Object create(Conf conf);

    /**
     * @see IBeanFactory#create(Class, BeanConfig, boolean, IBeanIniter)
     */
    Object create(BeanConfig cfg);

    /**
     * @see IBeanFactory#create(Class, Conf, boolean, IBeanIniter)
     */
    Object create(Conf conf, IBeanIniter initer);

    /**
     * @see IBeanFactory#create(Class, BeanConfig cfg, boolean, IBeanIniter)
     */
    Object create(BeanConfig cfg, IBeanIniter initer);

    /**
     * @see IBeanFactory#create(Class, BeanConfig, boolean, IBeanIniter)
     */
    <A> A create(Class<A> cls);

    /**
     * @see IBeanFactory#create(Class, Conf, boolean, IBeanIniter)
     */
    <A> A create(Class<A> cls, IBeanIniter initer);

    /**
     * Создать по имени
     */
    Object create(String name);

    /**
     * Создать по имени с инициализацией
     */
    Object create(String name, IBeanIniter initer);

}
