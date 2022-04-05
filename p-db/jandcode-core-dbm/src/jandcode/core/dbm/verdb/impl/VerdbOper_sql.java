package jandcode.core.dbm.verdb.impl;

import jandcode.core.dbm.verdb.*;

public class VerdbOper_sql extends BaseVerdbOper {

    public VerdbOper_sql(VerdbFile file, long versionNum3, String text) {
        super(file, versionNum3);
        setText(text);
    }
}
