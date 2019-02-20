package jandcode.commons.test;

/**
 * Предок для тестов с основными тестовыми сервисами.
 */
public abstract class Utils_Test extends Base_Test {

    public UtilsTestSvc utils = testSvc(UtilsTestSvc.class);
    public MemoryTestSvc memory = testSvc(MemoryTestSvc.class);
    public StopwatchTestSvc stopwatch = testSvc(StopwatchTestSvc.class);

}
