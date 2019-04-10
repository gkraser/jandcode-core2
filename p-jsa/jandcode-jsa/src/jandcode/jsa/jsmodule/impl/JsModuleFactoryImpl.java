package jandcode.jsa.jsmodule.impl;

import jandcode.commons.*;
import jandcode.commons.conf.*;
import jandcode.jsa.jsmodule.*;
import jandcode.core.*;
import jandcode.web.virtfile.*;

public class JsModuleFactoryImpl extends BaseComp implements JsModuleFactory {

    private String mask = "";
    private Class moduleClass;
    private Conf conf;

    protected void onConfigure(BeanConfig cfg) throws Exception {
        super.onConfigure(cfg);
        //
        this.conf = cfg.getConf();
    }

    public JsModule createInst(VirtFile f, Conf moduleCfg) {
        Conf cfg = UtConf.create();
        cfg.join(this.conf);
        if (moduleCfg != null) {
            cfg.join(moduleCfg);
        }
        cfg.setValue("class", null);
        JsModuleImpl m = (JsModuleImpl) getApp().create(getModuleClass());
        m.init(f, cfg);
        return m;
    }

    public Class getModuleClass() {
        return moduleClass;
    }

    public void setModuleClass(Class moduleClass) {
        this.moduleClass = moduleClass;
    }

    public String getMask() {
        return mask;
    }

    public void setMask(String mask) {
        this.mask = mask;
    }

    public boolean isCanCreate(String moduleName) {
        return UtVDir.matchPath(getMask(), moduleName);
    }

}
