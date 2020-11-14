package jandcode.core.db;

import jandcode.commons.named.*;

/**
 * Описание таблицы
 */
public interface DbMetadataTable extends INamed {

    /**
     * Поля таблицы
     */
    NamedList<DbMetadataField> getFields();

}
