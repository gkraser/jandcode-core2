package jandcode.jc.impl.lib;

import jandcode.jc.*;

/**
 * Простой провайдер по стандарту jc - каталог с библиотеками lib
 */
public abstract class LibProviderCustom implements ILibProvider {

    protected String path;
    protected ListLib libs;

    //////

    /**
     * Перекрыть для заполнения списка libs
     */
    protected abstract void doFillLibs(ListLib libs);

    //////

    protected void checkLoaded() {
        if (libs != null) {
            return;
        }
        ListLib tmp = new ListLib();
        doFillLibs(tmp);
        this.libs = tmp;
    }

    public String getPath() {
        return path;
    }

    public Lib findLib(String name) {
        checkLoaded();
        return libs.find(name);
    }

    public ListLib getLibs() {
        checkLoaded();
        ListLib tmp = new ListLib();
        tmp.addAll(libs);
        return tmp;
    }

    public String toString() {
        return getPath();
    }

    //////

}
