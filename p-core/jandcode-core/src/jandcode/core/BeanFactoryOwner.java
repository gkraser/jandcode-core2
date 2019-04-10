package jandcode.core;

import jandcode.commons.conf.*;

import java.util.*;

/**
 * Владелец beans.
 * Имеет собственную фабрику бинов и проедоставляет интерфейс для доступа к ней.
 */
public interface BeanFactoryOwner extends IBeanFactory, IBeanFactoryOwner, IBeanAccess {

    ////// default implementes ///////

    default <A> A create(Class<A> cls, Conf conf, boolean findClassInConf, IBeanIniter initer) {
        return getBeanFactory().create(cls, conf, findClassInConf, initer);
    }

    default <A> A create(Class<A> cls, BeanConfig cfg, boolean findClassInConf, IBeanIniter initer) {
        return getBeanFactory().create(cls, cfg, findClassInConf, initer);
    }

    default <A> A create(Conf conf, Class<A> cls, IBeanIniter initer) {
        return getBeanFactory().create(conf, cls, initer);
    }

    default <A> A create(BeanConfig cfg, Class<A> cls, IBeanIniter initer) {
        return getBeanFactory().create(cfg, cls, initer);
    }

    default <A> A create(Conf conf, Class<A> cls, boolean findClassInConf, IBeanIniter initer) {
        return getBeanFactory().create(conf, cls, findClassInConf, initer);
    }

    default <A> A create(BeanConfig cfg, Class<A> cls, boolean findClassInConf, IBeanIniter initer) {
        return getBeanFactory().create(cfg, cls, findClassInConf, initer);
    }

    default <A> A create(Conf conf, Class<A> cls) {
        return getBeanFactory().create(conf, cls);
    }

    default <A> A create(BeanConfig cfg, Class<A> cls) {
        return getBeanFactory().create(cfg, cls);
    }

    default <A> A create(Conf conf, Class<A> cls, boolean findClassInConf) {
        return getBeanFactory().create(conf, cls, findClassInConf);
    }

    default <A> A create(BeanConfig cfg, Class<A> cls, boolean findClassInConf) {
        return getBeanFactory().create(cfg, cls, findClassInConf);
    }

    default Object create(Conf conf) {
        return getBeanFactory().create(conf);
    }

    default Object create(BeanConfig cfg) {
        return getBeanFactory().create(cfg);
    }

    default Object create(Conf conf, IBeanIniter initer) {
        return getBeanFactory().create(conf, initer);
    }

    default Object create(BeanConfig cfg, IBeanIniter initer) {
        return getBeanFactory().create(cfg, initer);
    }

    default <A> A create(Class<A> cls) {
        return getBeanFactory().create(cls);
    }

    default <A> A create(Class<A> cls, IBeanIniter initer) {
        return getBeanFactory().create(cls, initer);
    }

    default Object create(String name) {
        return getBeanFactory().create(name);
    }

    default Object create(String name, IBeanIniter initer) {
        return getBeanFactory().create(name, initer);
    }

    ////// интерфейс доступа к объектам

    default <A> A bean(Class<A> cls) {
        return getBeanFactory().bean(cls);
    }

    default Object bean(String name) {
        return getBeanFactory().bean(name);
    }

    default <A> A inst(Class<A> cls) {
        return getBeanFactory().inst(cls);
    }

    default <A> List<A> impl(Class<A> cls) {
        return getBeanFactory().impl(cls);
    }

}
