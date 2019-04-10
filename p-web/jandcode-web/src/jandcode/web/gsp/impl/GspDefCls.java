package jandcode.web.gsp.impl;

import jandcode.core.*;
import jandcode.web.gsp.*;

/**
 * gsp объявленная как класс
 */
public class GspDefCls extends BaseComp implements GspDef {

    private Class cls;

    public GspDefCls(App app, Class cls) {
        setApp(app);
        this.cls = cls;
    }

    public Gsp createInst() {
        Gsp z = (Gsp) getApp().create(cls);
        z.setName(getName());
        return z;
    }

    public String getGspPath() {
        return "";
    }

    public String getGspSourceText() {
        return "";
    }

    public String getGspClassText() {
        return "";
    }

}
