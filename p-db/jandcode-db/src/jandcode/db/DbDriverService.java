package jandcode.db;

import jandcode.core.*;

import java.util.*;

/**
 * Драйвера базы данных
 */
public interface DbDriverService extends Comp {

    /**
     * Получить драйвер по имени
     */
    DbDriver getDbDriver(String name);

    /**
     * Имена зарегистрированных драйверов
     */
    Collection<String> getDbDriverNames();

}
