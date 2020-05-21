package jandcode.core.jsa.cfg.impl;

import jandcode.commons.*;
import jandcode.core.*;
import jandcode.core.jsa.cfg.*;
import jandcode.core.std.*;

public class JsaCfgImpl extends BaseCfg implements JsaCfg {

    private String bootModule;
    private String envModule;
    private String defaultTheme;
    private boolean minify;
    private boolean moduleSource;

    protected void onConfigure(BeanConfig cfg) throws Exception {
        super.onConfigure(cfg);
        //
        this.bootModule = getConf().getString("bootModule");
        this.envModule = getConf().getString("envModule");
        this.defaultTheme = getConf().getString("defaultTheme");

        String s;

        s = getConf().getString("minify");
        if (UtString.empty(s)) {
            this.minify = !getApp().getEnv().isDev();
        } else {
            this.minify = UtCnv.toBoolean(s);
        }

        s = getConf().getString("moduleSource");
        if (UtString.empty(s)) {
            this.moduleSource = getApp().getEnv().isDev();
        } else {
            this.moduleSource = UtCnv.toBoolean(s);
        }
    }

    public String getBootModule() {
        return bootModule;
    }

    public String getEnvModule() {
        return envModule;
    }

    public String getDefaultTheme() {
        return defaultTheme;
    }

    public boolean isMinify() {
        return minify;
    }

    public boolean isModuleSource() {
        return moduleSource;
    }
}
