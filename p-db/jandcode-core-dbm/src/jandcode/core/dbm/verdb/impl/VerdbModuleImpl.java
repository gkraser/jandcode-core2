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

    public List<VerdbOper> getOpers(VerdbVersion curVersion, VerdbVersion lastVersion) {
        List<VerdbOper> res = new ArrayList<>();

        dirs:
        for (VerdbDir dir : getDirs()) {
            if (dir.getVersion().getV1() >= curVersion.getV1()) {
                // только после версии каталога больше или равной текущей
                for (VerdbFile file : dir.getFiles()) {
                    for (VerdbOper oper : file.getOpers()) {
                        int cmpOper = oper.getVersion().compareTo(curVersion);
                        if (cmpOper > 0) {
                            // только для версий больше текущей
                            if (lastVersion != null) {
                                cmpOper = oper.getVersion().compareTo(lastVersion);
                                if (cmpOper > 0) {
                                    // прерываемся - вышли за границу
                                    break dirs;
                                }
                            }
                            res.add(oper);
                        }
                    }
                }
            }
        }

        return res;
    }
}
