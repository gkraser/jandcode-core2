package jandcode.core.launcher.impl;

import jandcode.commons.*;
import jandcode.core.launcher.*;

public class OptImpl implements Opt {

    private String name;
    private String desc = "";
    private String argName = "";
    private String defaultArgValue = "";

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public boolean hasArg() {
        return !UtString.empty(argName);
    }

    public String getArgName() {
        return argName;
    }

    public void setArgName(String argName) {
        this.argName = argName;
    }

    public String getDefaultArgValue() {
        return defaultArgValue;
    }

    public void setDefaultArgValue(String defaultArgValue) {
        this.defaultArgValue = defaultArgValue;
    }

}
