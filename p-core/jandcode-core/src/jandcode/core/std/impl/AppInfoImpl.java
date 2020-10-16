package jandcode.core.std.impl;

import jandcode.commons.*;
import jandcode.commons.conf.*;
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
        String mm = getApp().getConf().getString("app/mainmodule");
        if (UtString.empty(mm)) {
            // берем последний, которые идентифицируется как приложение
            for (ModuleInst m : getApp().getModules()) {
                if (m.getName().equals(AppConsts.MODULE_APP)) {
                    // app.cfx игнорируем
                    continue;
                }
                String s = m.getConf().getString("app/title");
                if (!UtString.empty(s)) {
                    mm = m.getName();
                }
            }
        }

        if (UtString.empty(mm)) {
            // ничего нет, вынужденная мера
            mm = "jandcode.core";
        }

        this.mainModule = getApp().getModules().get(mm);
        this.conf = UtConf.create();
        this.conf.join(this.mainModule.getConf().getConf("app"));
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
}
