package jandcode.jc.impl.lib;

import jandcode.jc.*;

import java.util.*;

/**
 * Простой провайдер по стандарту jc - каталог с библиотеками lib
 */
public class LibProviderStd extends LibProviderCustom {

    private Ctx ctx;

    public LibProviderStd(Ctx ctx, String path) {
        this.ctx = ctx;
        this.path = path;
    }

    protected void doFillLibs(ListLib libs) {
        LibDirLoader loader = new LibDirLoader(ctx, path);
        List<Lib> tmpLst = loader.load();
        for (Lib lib : tmpLst) {
            libs.add(lib);
        }
    }

}
