package jandcode.core.dbm.verdb.impl;

import jandcode.core.dbm.verdb.*;

import java.util.*;

public class VerdbFileImpl extends BaseVerdbItem implements VerdbFile {

    private VerdbDir dir;
    private List<VerdbOper> opers = new ArrayList<>();

    public VerdbFileImpl(VerdbDir dir, String path, long versionNum1, long versionNum2) {
        setPath(path);
        this.dir = dir;
        this.setVersion(VerdbVersion.create(versionNum1, versionNum2, 0));
    }

    public VerdbDir getDir() {
        return dir;
    }

    public List<VerdbOper> getOpers() {
        return opers;
    }

    public VerdbVersion getLastVersion() {
        if (getOpers().size() == 0) {
            return getVersion();
        }
        return getOpers().get(getOpers().size() - 1).getVersion();
    }
}
