package jandcode.web.render.impl;

import jandcode.commons.conf.*;
import jandcode.commons.named.*;
import jandcode.core.*;
import jandcode.web.render.*;

/**
 * render описанный в conf
 */
public class RenderDefConf extends BaseComp implements RenderDef {

    private Conf conf;

    public RenderDefConf(App app, Conf conf) {
        setApp(app);
        this.conf = conf;
        setName(conf.getName());
    }

    public IRender createInst() {
        IRender r = (IRender) getApp().create(conf);
        if (r instanceof INamedSet) {
            ((INamedSet) r).setName(getName());
        }
        return r;
    }

}
