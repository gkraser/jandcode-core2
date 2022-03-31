package jandcode.core.dbm.verdb.impl;

import jandcode.core.dbm.verdb.*;

public class BaseVerdbItem implements IVerdbVersionLink {

    private VerdbVersion version;
    private String path;

    public VerdbVersion getVersion() {
        return version;
    }

    protected void setVersion(VerdbVersion version) {
        this.version = version;
    }

    public int compareTo(IVerdbVersionLink o) {
        return getVersion().compareTo(o.getVersion());
    }

    public String getPath() {
        return path;
    }

    protected void setPath(String path) {
        this.path = path;
    }
}
