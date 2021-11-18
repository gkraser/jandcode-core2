package jandcode.core.apx.test;

import jandcode.core.dbm.test.*;
import jandcode.core.web.test.*;

/**
 * Предок для тестов с поддержкой apx, включая app, dbm, web
 */
public abstract class Apx_Test extends Dbm_Test {

    public WebTestSvc web = testSvc(WebTestSvc.class);
    public ApxTestSvc apx = testSvc(ApxTestSvc.class);

}
