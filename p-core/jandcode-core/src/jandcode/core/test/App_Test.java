package jandcode.core.test;

import jandcode.commons.test.*;

/**
 * Предок для тестов с поддержкой приложения
 */
public abstract class App_Test extends Utils_Test {

    public AppTestSvc app = testSvc(AppTestSvc.class);

}
