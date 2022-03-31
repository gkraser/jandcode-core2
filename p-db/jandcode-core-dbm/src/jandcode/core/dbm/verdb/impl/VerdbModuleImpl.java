package jandcode.core.dbm.verdb.impl;

import jandcode.commons.*;
import jandcode.core.*;
import jandcode.core.dbm.*;
import jandcode.core.dbm.verdb.*;

public class VerdbModuleImpl extends BaseModelMember implements VerdbModule {

    private String path;
    private String moduleName;

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = UtApp.getFileObject(getApp(), path).toString();
    }

    public String getModuleName() {
        if (UtString.empty(this.moduleName)) {
            return getModel().getModelDef().getInstanceOf().getName();
        }
        return moduleName;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }
}
