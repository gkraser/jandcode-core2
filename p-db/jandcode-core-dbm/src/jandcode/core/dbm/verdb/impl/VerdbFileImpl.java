package jandcode.core.dbm.verdb.impl;

import jandcode.core.dbm.verdb.*;

public class VerdbFileImpl extends BaseVerdbItem implements VerdbFile {

    private VerdbDir dir;

    public VerdbFileImpl(VerdbDir dir, String path, long versionNum1, long versionNum2) {
        setPath(path);
        this.dir = dir;
        this.setVersion(VerdbVersion.create(versionNum1, versionNum2, 0));
    }

    public VerdbDir getDir() {
        return dir;
    }
}
