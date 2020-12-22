package jandcode.core.std.impl;

import jandcode.commons.conf.*;
import jandcode.commons.variant.*;
import jandcode.core.*;
import jandcode.core.std.*;

public abstract class CfgImpl extends BaseComp implements Cfg, IVariantNamed {

    public CfgService getCfgService() {
        return getApp().bean(CfgService.class);
    }

    public Conf getConf() {
        return getCfgService().getConf();
    }

    public Object getValue(String name) {
        return getConf().getValue(name);
    }

}
