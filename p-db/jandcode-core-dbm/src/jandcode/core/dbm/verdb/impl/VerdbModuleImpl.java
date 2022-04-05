package jandcode.core.dbm.verdb.impl;

import jandcode.core.dbm.*;
import jandcode.core.dbm.verdb.*;

import java.util.*;

public class VerdbModuleImpl extends BaseModelMember implements VerdbModule {

    private String path;
    private String moduleName;
    private List<VerdbDir> dirs;

    public VerdbModuleImpl(Model model, String path, String moduleName) {
        setModel(model);
        this.path = path;
        this.moduleName = moduleName;
    }

    public String getPath() {
        return path;
    }

    public String getModuleName() {
        return moduleName;
    }

    public List<VerdbDir> getDirs() {
        if (dirs == null) {
            synchronized (this) {
                if (dirs == null) {
                    VerdbDirLoader ldr = new VerdbDirLoader();
                    List<VerdbDir> tmp = ldr.loadDir(getPath(), this);
                    dirs = tmp;
                }
            }
        }
        return dirs;
    }

}
