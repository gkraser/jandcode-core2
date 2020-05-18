package jandcode.core.jsa.jc.impl;


import jandcode.core.jsa.jc.*;

/**
 * Базовый предок для провайдеров nodejs-библиотек.
 */
public abstract class BaseNodeJsLibProvider implements NodeJsLibProvider {

    protected String path;
    protected NodeJsLibList libs;

    //////

    /**
     * Перекрыть для заполнения списка libs
     */
    protected abstract void doFillLibs(NodeJsLibList libs);

    //////

    protected void checkLoaded() {
        if (libs != null) {
            return;
        }
        NodeJsLibList tmp = new NodeJsLibList();
        doFillLibs(tmp);
        this.libs = tmp;
    }

    public String getPath() {
        return path;
    }

    public NodeJsLib findLib(String name) {
        checkLoaded();
        return libs.find(name);
    }

    public NodeJsLibList getLibs() {
        checkLoaded();
        NodeJsLibList tmp = new NodeJsLibList();
        tmp.addAll(libs);
        return tmp;
    }

    public String toString() {
        return getPath();
    }

    //////

}
