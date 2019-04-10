package jandcode.jc.std.junit;

/**
 * Интерфейс для скриптов запускалок тестов
 */
public interface IJUnitRunner {

    /**
     * Запуск unit-тестов в контексте JUnitProject и JavaProject.
     *
     * @param testClassName имя класса с тестами. Если не указано - все класы в тестах
     * @param testMethods   методы класса через ','. Если не указаны, но указн класс,
     *                      то все методы класса
     */
    boolean runTests(String testClassName, String testMethods);

}
