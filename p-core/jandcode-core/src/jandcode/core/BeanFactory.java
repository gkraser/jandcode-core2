package jandcode.core;

import jandcode.commons.conf.*;

import java.util.*;

/**
 * Хранилище и фабрика бинов
 */
public interface BeanFactory extends IBeanFactory, IBeanIniter, IBeanAccess, IBeanConfigure {

    /**
     * Возвращает копию списка бинов.
     * Обычно используется в dev-режиме для нализа состояния хранилища.
     * В production не используется.
     */
    List<BeanDef> getBeans();

    /**
     * Найти описание бина.
     *
     * @return null, если не найден
     */
    BeanDef findBean(String name);

    /**
     * Найти описание бина.
     *
     * @param cls          имя класса для бина, соотсветствует имени бина
     * @param autoRegister true - если бин не найден, зарегистририровать бин с именем
     *                     как у класса cls и с реализатором для класса cls
     * @return null, если не найден
     */
    BeanDef findBean(Class cls, boolean autoRegister);

    /**
     * Получить описание бина.
     *
     * @return ошибка, если не найден
     */
    BeanDef getBean(String name);

    /**
     * Получить описание бина.
     *
     * @return ошибка, если не найден
     */
    BeanDef getBean(Class cls);

    /**
     * Зарегистрировать бин
     *
     * @param name имя
     * @param cls  класс
     * @return описание бина
     */
    BeanDef registerBean(String name, Class cls);

    /**
     * Зарегистрировать бин как экземпляр
     *
     * @param name имя
     * @param inst экземпляр объекта
     * @return описание бина
     */
    BeanDef registerBean(String name, Object inst);

    /**
     * Зарегистрировать бин
     *
     * @param conf конфигурация бина. Имя conf - имя бина, атрибут class - класс.
     */
    BeanDef registerBean(Conf conf);

    /**
     * Зарегистрировать бины
     *
     * @param conf конфигурация набора бинов. Каждый свойство типа conf - описание бина
     */
    List<BeanDef> registerBeans(Conf conf);

    /**
     * Ссылка на хранилище, которое создало это.
     * Используется при инициализации объектов.
     */
    BeanFactory getParentBeanFactory();

    /**
     * Установить родительское хранилище.
     */
    void setParentBeanFactory(BeanFactory b);

    /**
     * Инициализатор по умолчанию
     */
    IBeanIniter getDefaultBeanIniter();

    /**
     * Установить инициализатор по умолчанию
     */
    void setDefaultBeanIniter(IBeanIniter defaultBeanIniter);

    /**
     * Возвращает класс по имени.
     * В процессе поиска используются зарегистрированные бины и родительские хранилища.
     */
    Class getClass(String name);

}

