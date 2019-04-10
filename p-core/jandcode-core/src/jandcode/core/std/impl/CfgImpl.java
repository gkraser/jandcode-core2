package jandcode.core.std.impl;

import jandcode.commons.*;
import jandcode.commons.conf.*;
import jandcode.core.*;
import jandcode.core.std.*;

public abstract class CfgImpl extends BaseComp implements Cfg {

    private Conf conf = UtConf.create();

    protected void onConfigure(BeanConfig cfg) throws Exception {
        super.onConfigure(cfg);
        //
        Conf conf = cfg.getConf();
        this.conf.join(conf);
        //
        String cfgPath = conf.getString("cfg");
        if (!UtString.empty(cfgPath)) {
            Conf aliasRt = getApp().getConf().findConf(cfgPath);
            if (aliasRt != null) {
                this.conf.join(aliasRt);
            }
        }
    }

    //////

    public Conf getConf() {
        return conf;
    }

}
