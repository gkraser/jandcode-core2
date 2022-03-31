package jandcode.core.dbm.verdb.impl;

import jandcode.core.dbm.verdb.*;

import java.util.*;

public class VerdbDirImpl extends BaseVerdbItem implements VerdbDir {

    private Collection<VerdbFile> files = new ArrayList<>();

    public VerdbDirImpl(String path, long versionNum) {
        setPath(path);
        this.setVersion(VerdbVersion.create(versionNum, 0, 0));
    }

    public Collection<VerdbFile> getFiles() {
        return files;
    }

    public void setFiles(Collection<VerdbFile> files) {
        this.files.clear();
        this.files.addAll(files);
    }

}
