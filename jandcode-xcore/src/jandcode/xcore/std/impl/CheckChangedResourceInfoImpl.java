package jandcode.xcore.std.impl;

import jandcode.xcore.std.*;

public class CheckChangedResourceInfoImpl implements CheckChangedResourceInfo {

    private boolean needRestartApp;

    public boolean isNeedRestartApp() {
        return needRestartApp;
    }

    public void needRestartApp() {
        this.needRestartApp = true;
    }

    public void clear() {
        this.needRestartApp = false;
    }

}
