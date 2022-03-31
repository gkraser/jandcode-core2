package jandcode.core.dbm.verdb.impl;

import jandcode.core.dbm.verdb.*;

public class VerdbFileImpl extends BaseVerdbItem implements VerdbFile {

    public VerdbFileImpl(String path, long versionNum1, long versionNum2) {
        setPath(path);
        this.setVersion(VerdbVersion.create(versionNum1, versionNum2, 0));
    }

}
