package jandcode.core.std.impl;

import jandcode.commons.*;
import jandcode.commons.conf.*;
import jandcode.commons.datetime.*;
import jandcode.core.*;
import jandcode.core.std.*;

public class AppInfoImpl extends BaseComp implements AppInfo {

    private ModuleInst mainModule;
    private Conf conf;

    protected void checkMainModule() {
        if (mainModule == null) {
            synchronized (this) {
                if (mainModule == null) {
                    resolveMainModule();
                }
            }
        }
    }

    protected void resolveMainModule() {
        Conf appConf = getApp().getConf().getConf("app");

        String mm = appConf.getString("mainmodule");

        if (UtString.empty(mm)) {
            // ничего нет, вынужденная мера
            mm = "jandcode.core";
        }

        this.mainModule = getApp().getModules().get(mm);
        this.conf = Conf.create();
        this.conf.join(appConf);
    }

    public String getMainModule() {
        checkMainModule();
        return mainModule.getName();
    }

    public String getTitle() {
        checkMainModule();
        return conf.getString("title");
    }

    public String getCopyright() {
        checkMainModule();
        return conf.getString("copyright");
    }

    public String getVersion() {
        return UtVersion.getVersion(getMainModule());
    }

    public Conf getConf() {
        return conf;
    }

    public XDateTime getBuildDate() {
        return UtVersion.getBuildDate(getMainModule());
    }

}
