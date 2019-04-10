package jandcode.core.test;

import jandcode.commons.test.*;

/**
 * Предок для сервисов тестов, которые работают в контексте приложения
 */
public abstract class BaseAppTestSvc extends BaseTestSvc {

    protected UtilsTestSvc utils;
    public MemoryTestSvc memory;
    public StopwatchTestSvc stopwatch;

    public void setUp() throws Exception {
        super.setUp();
        this.utils = testSvc(UtilsTestSvc.class);
        this.memory = testSvc(MemoryTestSvc.class);
        this.stopwatch = testSvc(StopwatchTestSvc.class);
    }

    /**
     * Приложение
     */
    public AppTestSvc getApp() {
        return testSvc(AppTestSvc.class);
    }

}
