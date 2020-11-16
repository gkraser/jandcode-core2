package jandcode.core.db;

import jandcode.commons.named.*;
import jandcode.core.*;

/**
 * Сервис метаданных базы данных
 */
public interface DbMetadataService extends Comp, IDbSourceMember {

    /**
     * Таблицы
     */
    NamedList<DbMetadataTable> getTables();


    /**
     * Сброс. Очистка кеша.
     * После вызова данные будут загружены повторно.
     */
    void reset();

}
