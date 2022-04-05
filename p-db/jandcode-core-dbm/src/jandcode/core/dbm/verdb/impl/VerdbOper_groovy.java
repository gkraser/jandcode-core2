package jandcode.core.dbm.verdb.impl;

import jandcode.core.dbm.mdb.*;
import jandcode.core.dbm.verdb.*;

public class VerdbOper_groovy extends BaseVerdbOper {

    public VerdbOper_groovy(VerdbFile file, long versionNum3, String text) {
        super(file, versionNum3);
        setText(text);
    }

    public void exec(Mdb mdb) throws Exception {
        // todo: not implemented exec
    }
}
