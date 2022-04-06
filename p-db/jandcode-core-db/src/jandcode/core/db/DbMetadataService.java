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

}
