package jandcode.db;

import jandcode.core.*;

/**
 * Сервис для управления базой данных (создание/удаление)
 */
public interface DbManagerService extends Comp, IDbSourceMember {

    /**
     * Существует ли база данных
     */
    boolean existDatabase() throws Exception;

    /**
     * Создание базы данных
     */
    void createDatabase() throws Exception;

    /**
     * Удаление базы данных
     */
    void dropDatabase() throws Exception;

}
