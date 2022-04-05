package jandcode.core.dbm.verdb.impl;

import jandcode.core.dbm.mdb.*;
import jandcode.core.dbm.verdb.*;

/**
 * Оператор-заглушка, ничего не делает.
 */
public class VerdbOper_dummy extends BaseVerdbOper {

    public VerdbOper_dummy(VerdbFile file, long versionNum3) {
        super(file, versionNum3);
    }

    public void exec(Mdb mdb) throws Exception {
        // ничего не делаем
    }

}
