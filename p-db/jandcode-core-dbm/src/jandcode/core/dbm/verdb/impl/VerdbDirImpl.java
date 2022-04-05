package jandcode.core.dbm.verdb.impl;

import jandcode.core.dbm.verdb.*;

import java.util.*;

public class VerdbDirImpl extends BaseVerdbItem implements VerdbDir {

    private List<VerdbFile> files = new ArrayList<>();
    private VerdbModule module;

    public VerdbDirImpl(VerdbModule module, String path, long versionNum) {
        setPath(path);
        this.setVersion(VerdbVersion.create(versionNum, 0, 0));
        this.module = module;
    }

    public VerdbModule getModule() {
        return module;
    }

    public List<VerdbFile> getFiles() {
        return files;
    }

    public void setFiles(Collection<VerdbFile> files) {
        this.files.clear();
        this.files.addAll(files);
    }

    public VerdbVersion getLastVersion() {
        if (getFiles().size() == 0) {
            return getVersion();
        } else {
            VerdbFile f = getFiles().get(getFiles().size() - 1);
            return f.getLastVersion();
        }
    }
}
