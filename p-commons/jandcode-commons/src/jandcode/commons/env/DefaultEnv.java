package jandcode.commons.env;

import jandcode.commons.variant.*;

import java.util.*;

/**
 * Реализация {@link Env} по умолчанию.
 */
public class DefaultEnv implements Env {

    private boolean dev;
    private boolean source;
    private boolean test;
    private IVariantMap properties = new VariantMap();

    public DefaultEnv(boolean dev, boolean source, boolean test, Map<String, Object> properties) {
        this.dev = dev;
        this.source = source;
        this.test = test;
        if (properties != null) {
            this.properties.putAll(properties);
        }
    }

    public boolean isDev() {
        return dev;
    }

    public boolean isSource() {
        return source;
    }

    public boolean isTest() {
        return test;
    }

    public IVariantMap getProperties() {
        return properties;
    }

}
