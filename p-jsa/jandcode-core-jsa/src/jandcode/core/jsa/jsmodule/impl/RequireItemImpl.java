package jandcode.core.jsa.jsmodule.impl;

import jandcode.core.jsa.jsmodule.*;

import java.util.*;

public class RequireItemImpl implements RequireItem {

    private String usedName;
    private List<String> realNames;

    public RequireItemImpl(String usedName, List<String> realNames) {
        this.usedName = usedName;
        this.realNames = realNames;
    }

    public String getUsedName() {
        return usedName;
    }

    public List<String> getRealNames() {
        return realNames;
    }

    public String toString() {
        return getUsedName() + "=>" + getRealNames();
    }
}
