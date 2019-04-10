package jandcode.core;

import jandcode.core.impl.*;

/**
 * Хранилище бинов по умолчанию
 */
public class DefaultBeanFactory extends BeanFactoryImpl implements BeanFactory {

    public DefaultBeanFactory() {
    }

    public DefaultBeanFactory(IBeanIniter initer) {
        setDefaultBeanIniter(initer);
    }

}
