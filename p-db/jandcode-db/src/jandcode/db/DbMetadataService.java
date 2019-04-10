package jandcode.db;

import jandcode.core.*;
import jandcode.commons.named.*;

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
