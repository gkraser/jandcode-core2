package jandcode.commons.test;

/**
 * Предок для сервисов тестов, которые работают с утилитамм
 */
public abstract class BaseUtilsTestSvc extends BaseTestSvc {

    protected UtilsTestSvc utils;
    public MemoryTestSvc memory;
    public StopwatchTestSvc stopwatch;

    public void setUp() throws Exception {
        super.setUp();
        this.utils = testSvc(UtilsTestSvc.class);
        this.memory = testSvc(MemoryTestSvc.class);
        this.stopwatch = testSvc(StopwatchTestSvc.class);
    }

}
