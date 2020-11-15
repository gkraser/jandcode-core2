package jandcode.core.db;

import jandcode.commons.conf.*;
import jandcode.core.*;

import java.util.*;

/**
 * Сервис для баз данных.
 */
public interface DbService extends Comp {

    /**
     * Имена зарегистрированных DbSource.
     * Фактически имена из конфигурации 'cfg/dbsource'.
     */
    Collection<String> getDbSourceNames();

    /**
     * Создает новый экземпляр DbSource по переданной conf,
     * которая возможно загружена из конфига или создана на лету.
     */
    DbSource createDbSource(Conf conf);

    /**
     * Создает новый экземпляр DbSource по имени.
     * Конфигурацяи загружается из 'cfg/dbsource/NAME'.
     */
    DbSource createDbSource(String name);

    /**
     * Возвращает экземпляр DbSource по имени.
     * Конфигурацяи загружается из 'cfg/dbsource/NAME'.
     */
    DbSource getDbSource(String name);

}
