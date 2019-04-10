package jandcode.db;

import jandcode.core.*;
import jandcode.commons.named.*;
import jandcode.commons.conf.*;

/**
 * Сервис для баз данных.
 */
public interface DbService extends Comp {

    /**
     * Зарегистрированные dbsource
     */
    NamedList<DbSourceDef> getDbSources();

    /**
     * Зарегистрированные dbdriver
     */
    NamedList<DbDriverDef> getDbDrivers();

    /**
     * Определить dbdriver, который будет использован, если по x будет создана dbsource
     */
    DbDriverDef resolveDbDriver(Conf x);

    /**
     * Создает новый экземпляр DbSource по настройкам из app.cfx 'dbsource/DBSOURCENAME'
     */
    DbSource createDbSource(String dbsourceName);

    /**
     * Создает новый экземпляр DbSource по переданной conf.
     * Должна быть полностью сформирована, со всеми нужными атрибутами.
     */
    DbSource createDbSource(Conf conf);

    /**
     * Возвращает кешированный экземпляр DbSource по настройкам из app.cfx 'dbsource/DBSOURCENAME'
     */
    DbSource getDbSource(String dbsourceName);

    /**
     * Возвращает кешированный экземпляр DbSource по настройкам из app.cfx 'dbsource/DBSOURCENAME'
     * с именем default
     */
    default DbSource getDbSource() {
        return getDbSource(DbConsts.DEFAULT);
    }

    /**
     * Возвращает новый экземпляр Db для dbsource с именем dbsourceName.
     * Соединение автоматически не устанавливается.
     */
    default Db createDb(String dbsourceName) {
        return getDbSource(dbsourceName).createDb();
    }

    /**
     * Возвращает кешированный в рамках текущего потока экземпляр Db
     * для dbsource с именем dbsourceName.
     * Соединение автоматически не устанавливается.
     */
    default Db getDb(String dbsourceName) {
        return getDbSource(dbsourceName).getDb();
    }

    /**
     * Возвращает кешированный в рамках текущего потока экземпляр Db
     * для dbsource с именем default.
     * Соединение автоматически не устанавливается.
     */
    default Db getDb() {
        return getDb(DbConsts.DEFAULT);
    }

}
