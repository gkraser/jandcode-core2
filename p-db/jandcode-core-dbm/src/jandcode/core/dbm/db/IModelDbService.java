package jandcode.core.dbm.db;

import jandcode.core.db.*;

/**
 * Методы модели, связанные с базой данных
 */
public interface IModelDbService {

    /**
     * DbSource, принадлежащее модели.
     * Описана в dbsource с именем default.
     * Модель имеет только один dbsource.
     */
    DbSource getDbSource();

    //////

    /**
     * Тип базы данных. Например 'oracle', 'mysql'.
     */
    default String getDbType() {
        return getDbSource().getDbType();
    }

    /**
     * Возвращает новый экземпляр Db.
     * Соединение автоматически не устанавливается.
     */
    default Db createDb() {
        return getDbSource().createDb();
    }

    /**
     * Возвращает новый экземпляр Db.
     * Соединение автоматически не устанавливается.
     *
     * @param direct при значении true создается экземпляр, настроенный
     *               на direct-соединения (без пула).
     */
    default Db createDb(boolean direct) {
        return getDbSource().createDb(direct);
    }

    /**
     * Возвращает кешированный в рамках текущего потока экземпляр Db.
     * Соединение автоматически не устанавливается.
     */
    @Deprecated
    Db getDb();

}
