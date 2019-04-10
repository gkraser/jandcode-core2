package jandcode.commons.test;

import jandcode.commons.error.*;

/**
 * Базовый класс для сервиса тестов
 */
public abstract class BaseTestSvc {

    private Base_Test test;

    /**
     * setUp для перекрытия в потомках
     */
    public void setUp() throws Exception {
    }


    /**
     * tearDown для перекрытия в потомках
     */
    public void tearDown() throws Exception {
    }

    /**
     * Тест, в рамках которого работает сервис
     */
    public Base_Test getTest() {
        if (test == null) {
            throw new XError("test not assigned");
        }
        return test;
    }

    void setTest(Base_Test test) {
        this.test = test;
    }

    //////

    /**
     * Получить экземпляр тестового сервиса по классу.
     * Экземпляр сервиса создается при первом обращении автоматически.
     */
    public <A extends BaseTestSvc> A testSvc(Class<A> cls) {
        if (test == null) {
            throw new XError("Метод testSvc можно вызывать в setUp или внутри тестового метода");
        }
        return getTest().testSvc(cls);
    }

}
