package jandcode.core.dbm.verdb.impl;

import jandcode.core.dbm.verdb.*;

public abstract class BaseVerdbOper extends BaseVerdbItem implements VerdbOper {

    private VerdbFile file;
    private String text;

    public BaseVerdbOper(VerdbFile file, long versionNum3) {
        this.file = file;
        setVersion(file.getVersion().with(-1, -1, versionNum3));
    }

    public VerdbFile getFile() {
        return file;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

}
