package jandcode.jc.nodejs.impl;


import jandcode.commons.named.*;
import jandcode.jc.nodejs.*;

/**
 * Базовый предок для провайдеров nodejs-модулей.
 */
public abstract class BaseNodeJsModuleProvider implements NodeJsModuleProvider {

    protected String path;
    protected NamedList<NodeJsModule> modules;

    //////

    /**
     * Перекрыть для заполнения списка modules
     */
    protected abstract void doFillLibs(NamedList<NodeJsModule> modules);

    //////

    protected void checkLoaded() {
        if (modules != null) {
            return;
        }
        NamedList<NodeJsModule> tmp = new DefaultNamedList<>();
        doFillLibs(tmp);
        this.modules = tmp;
    }

    public String getPath() {
        return path;
    }

    public NodeJsModule findModule(String name) {
        checkLoaded();
        return modules.find(name);
    }

    public NamedList<NodeJsModule> getModules() {
        checkLoaded();
        NamedList<NodeJsModule> tmp = new DefaultNamedList<>();
        tmp.addAll(modules);
        return tmp;
    }

    public String toString() {
        return getPath();
    }

    //////

}
