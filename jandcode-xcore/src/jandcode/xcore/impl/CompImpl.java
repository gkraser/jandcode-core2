package jandcode.xcore.impl;

import jandcode.commons.error.*;
import jandcode.commons.named.*;
import jandcode.xcore.*;

public abstract class CompImpl extends Named implements Comp {

    private App app;

    //////

    public App getApp() {
        if (app == null) {
            throw new XError("Экземпляр компонента создан вне контекста приложения: {0}", getClass());
        }
        return app;
    }

    public void setApp(App app) {
        this.app = app;
    }

    public void beanConfigure(BeanConfig cfg) throws Exception {
        if (!hasName() && cfg.hasName()) {
            setName(cfg.getName());
        }
        onConfigure(cfg);
    }

    /**
     * Реализация конфигурирования
     */
    protected abstract void onConfigure(BeanConfig cfg) throws Exception;

}
