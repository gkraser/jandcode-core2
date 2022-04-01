package jandcode.core.dbm.test;

import jandcode.core.dbm.*;
import jandcode.core.dbm.mdb.*;
import jandcode.core.test.*;

/**
 * Предок для тестов с поддержкой dbm
 */
public abstract class Dbm_Test extends App_Test {

    public DbmTestSvc dbm = testSvc(DbmTestSvc.class);

    public Model getModel() {
        return dbm.getModel();
    }

    public Mdb getMdb() {
        return dbm.getMdb();
    }

}
