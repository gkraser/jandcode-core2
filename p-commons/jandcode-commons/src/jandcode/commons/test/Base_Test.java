package jandcode.commons.test;

import jandcode.commons.*;
import jandcode.commons.error.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.*;

import java.util.*;

/**
 * Базовый класс для unit-тестов
 */
public abstract class Base_Test {

    /**
     * Глобальный номер выполняемого теста
     */
    public static int testNum = 0;

    /**
     * Текущее имя тестового метода
     */
    private String testName;

    private boolean setUpRunned;

    private Map<Class, BaseTestSvc> testSvcs = new LinkedHashMap<>();
    private Set<Object> showedErrors = new HashSet<>();

    /**
     * Вызывается при неудачном тесте - показывает ошибку
     */
    class FailedListener implements AfterTestExecutionCallback, AfterEachCallback {

        public void afterTestExecution(ExtensionContext context) throws Exception {
            Optional<Throwable> ex = context.getExecutionException();
            if (ex.isPresent()) {
                UtilsTestSvc utils = new UtilsTestSvc();
                utils.setTest(Base_Test.this);
                utils.showError(ex.get());
                showedErrors.add(ex.get());
            }
        }

        public void afterEach(ExtensionContext context) throws Exception {
            Optional<Throwable> ex = context.getExecutionException();
            if (ex.isPresent()) {
                if (!showedErrors.contains(ex.get())) { // может уже показали
                    UtilsTestSvc utils = new UtilsTestSvc();
                    utils.setTest(Base_Test.this);
                    utils.showError(ex.get());
                    showedErrors.add(ex.get());
                }
            }
        }

    }

    @RegisterExtension
    public FailedListener failedListener = new FailedListener();


    /**
     * Внутренний setUp
     */
    @BeforeEach
    public void internal_setUp(TestInfo testInfo) throws Exception {
        this.setUpRunned = true;
        testNum++;
        if (testInfo.getTestMethod().isPresent()) {
            this.testName = testInfo.getTestMethod().get().getName();
        }
        //
        for (BaseTestSvc u : this.testSvcs.values()) {
            u.setUp();
        }
        //
        setUp();
    }

    /**
     * setUp для перекрытия в потомках
     */
    public void setUp() throws Exception {
    }


    /**
     * Внутренний tearDown
     */
    @AfterEach
    public void internal_tearDown() throws Exception {
        tearDown();
        //
        for (BaseTestSvc u : this.testSvcs.values()) {
            u.tearDown();
        }
        //
    }

    /**
     * tearDown для перекрытия в потомках
     */
    public void tearDown() throws Exception {
    }

    //////

    /**
     * Имя текущего тестового метода
     */
    public String getTestName() {
        if (testName == null) {
            return "NONAME"; //NON-NLS
        }
        return testName;
    }

    /**
     * Получить экземпляр тестового сервиса по классу.
     * Экземпляр сервиса создается при первом обращении автоматически.
     */
    @SuppressWarnings("unchecked")
    public <A extends BaseTestSvc> A testSvc(Class<A> cls) {
        BaseTestSvc res = this.testSvcs.get(cls);
        if (res != null) {
            return (A) res;
        }
        res = (BaseTestSvc) UtClass.createInst(cls);
        res.setTest(this);
        this.testSvcs.put(cls, res);
        if (setUpRunned) {
            try {
                res.setUp();
            } catch (Exception e) {
                throw new XErrorWrap(e);
            }
        }
        return (A) res;
    }

}

