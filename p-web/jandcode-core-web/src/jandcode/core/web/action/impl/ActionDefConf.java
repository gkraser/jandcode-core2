package jandcode.core.web.action.impl;

import jandcode.commons.conf.*;
import jandcode.commons.named.*;
import jandcode.core.*;
import jandcode.core.web.action.*;

/**
 * action описанная в conf.
 * Если определен cls, то он используется только если нет явного class
 * в конфигурации.
 */
public class ActionDefConf extends CustomActionDef {

    private Conf conf;
    private Class cls;

    public ActionDefConf(App app, Conf conf, String name, Class cls) {
        setApp(app);
        this.conf = conf;
        this.cls = cls;
        setName(name);
    }

    public IAction createInst() {
        IAction a = (IAction) getApp().create(conf, this.cls, true);
        if (a instanceof INamedSet) {
            ((INamedSet) a).setName(getName());
        }
        return a;
    }


}
