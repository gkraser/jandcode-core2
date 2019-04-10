package jandcode.web.action.impl;

import jandcode.commons.conf.*;
import jandcode.commons.named.*;
import jandcode.core.*;
import jandcode.web.action.*;

/**
 * action описанная в conf
 */
public class ActionDefConf extends CustomActionDef {

    private Conf conf;

    public ActionDefConf(App app, Conf conf, String name) {
        setApp(app);
        this.conf = conf;
        setName(name);
    }

    public IAction createInst() {
        IAction a = (IAction) getApp().create(conf);
        if (a instanceof INamedSet) {
            ((INamedSet) a).setName(getName());
        }
        return a;
    }


}
