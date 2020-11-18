package jandcode.core.dbm.mdb;

import jandcode.core.db.*;

/**
 * Методы для сервиса с поддержкой mdb
 */
public interface IMdbService {

    /**
     * Создать объект с утилитами для модели и ее базы данных.
     */
    Mdb createMdb();

    /**
     * Создать объект с утилитами для модели и ее базы данных.
     *
     * @param direct при значении true создается экземпляр, настроенный
     *               на direct-соединения (без пула).
     */
    Mdb createMdb(boolean direct);

    /**
     * Создать объект с утилитами для модели и указанной базы данных.
     */
    Mdb createMdb(Db db);

}
