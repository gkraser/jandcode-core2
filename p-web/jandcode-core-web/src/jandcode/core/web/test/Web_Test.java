package jandcode.core.web.test;

import jandcode.core.test.*;

/**
 * Предок для web-тестов
 */
public abstract class Web_Test extends App_Test {

    public WebTestSvc web = testSvc(WebTestSvc.class);

}
