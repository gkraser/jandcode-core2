package jandcode.core.jsa.cfg.impl;

import jandcode.core.*;
import jandcode.core.jsa.cfg.*;
import jandcode.core.std.*;

public class JsaCfgImpl extends BaseCfg implements JsaCfg {

    private String bootModule;
    private String envModule;
    private String defaultTheme;

    protected void onConfigure(BeanConfig cfg) throws Exception {
        super.onConfigure(cfg);
        //
        this.bootModule = getConf().getString("bootModule");
        this.envModule = getConf().getString("envModule");
        this.defaultTheme = getConf().getString("defaultTheme");
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

}
