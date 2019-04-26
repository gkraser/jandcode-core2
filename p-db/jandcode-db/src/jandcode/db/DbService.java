package jandcode.db;

import jandcode.commons.conf.*;
import jandcode.core.*;

/**
 * Сервис для баз данных.
 */
public interface DbService extends Comp {

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
