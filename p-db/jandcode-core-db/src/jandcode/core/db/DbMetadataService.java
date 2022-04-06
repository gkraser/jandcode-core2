package jandcode.core.db;

import jandcode.commons.named.*;
import jandcode.core.*;

/**
 * Сервис метаданных базы данных
 */
public interface DbMetadataService extends Comp, IDbSourceMember {

    /**
     * Загрузить все таблицы/view из базы данных
     */
    NamedList<DbMetadataTable> loadTables() throws Exception;

    /**
     * Есть ли таблицы или view в базе данные.
     * Работает быстрее, дем laadTables() с проверкой на пустой список.
     */
    boolean hasTables() throws Exception;

    /**
     * Есть ли указанная таблица в базе данных.
     */
    boolean hasTable(String name) throws Exception;

}
