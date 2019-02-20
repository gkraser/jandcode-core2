package jandcode.xcore;

import jandcode.xcore.impl.*;

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
