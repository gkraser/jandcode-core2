package jandcode.core.dbm.fixture;

import jandcode.commons.conf.*;
import jandcode.core.*;

import java.util.*;

/**
 * Сервис fixture. Это сервис приложения, а не модели.
 */
public interface FixtureService extends Comp {

    /**
     * Доступные имена FixtureSuite.
     * Фактически имена из конфигурации 'dbm/fixture-suite'.
     */
    List<String> getFixtureSuiteNames();

    /**
     * Создать указанный {@link FixtureSuite}
     * из конфигурации 'dbm/fixture-suite/NAME'
     */
    FixtureSuite createFixtureSuite(String name);

    /**
     * Создать {@link FixtureSuite}
     * из конфигурации.
     */
    FixtureSuite createFixtureSuite(Conf conf);

}
