package jandcode.jc.impl.lib;

import jandcode.jc.*;

/**
 * Провайдер системных библиотек из jdk.
 * Автоматически создается первым.
 */
public class LibProviderSys extends LibProviderCustom {

    private Ctx ctx;

    public LibProviderSys(Ctx ctx) {
        this.ctx = ctx;
        path = System.getProperty("java.home");
    }

    protected void doFillLibs(ListLib libs) {
        libs.add(new LibJdkTools(ctx));
    }

}
