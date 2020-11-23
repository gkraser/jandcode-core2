package jandcode.core.apx.test;

import jandcode.core.dbm.test.*;

/**
 * Предок для тестов с поддержкой apx, включая app, dbm
 */
public abstract class Apx_Test extends Dbm_Test {

    public ApxTestSvc apx = testSvc(ApxTestSvc.class);

}
