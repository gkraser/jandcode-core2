package jandcode.xcore;

import jandcode.commons.conf.*;
import jandcode.xcore.impl.*;

/**
 * Реализация конфигурации по умолчанию для использования в конфигурировании сложных
 * дочерних компонентов.
 */
public class DefaultBeanConfig extends BeanConfigImpl {

    public DefaultBeanConfig() {
    }

    public DefaultBeanConfig(Conf conf, String name) {
        super(conf, name);
    }

    public DefaultBeanConfig(Conf conf) {
        super(conf);
    }

}
