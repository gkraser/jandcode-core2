package jandcode.core;

import jandcode.commons.conf.*;

public class AppConfHandler1 implements AppConfHandler {

    public void handleModuleConf(Module module, Module moduleOwner, Conf conf) throws Exception {
        String mn = module.getName();
        System.out.println("AppConfHander: " + conf.getName() + " for Module=" + mn);
        module.getConf().findConf("from-app-conf-handler/" + conf.getName() + "1" + mn, true);
    }

    public void handleAppConf(App app, Module moduleOwner, Conf conf) throws Exception {
        String mn = "";
        System.out.println("AppConfHander: " + conf.getName() + " for App");
        app.getConf().findConf("from-app-conf-handler/app-" + conf.getName() + "1" + mn, true);
    }
}
