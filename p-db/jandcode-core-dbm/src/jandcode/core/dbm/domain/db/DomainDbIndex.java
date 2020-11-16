package jandcode.core.dbm.domain.db;

import jandcode.commons.named.*;

import java.util.*;

/**
 * Индекс для домена в базе данных
 */
public interface DomainDbIndex extends INamed {

    /**
     * Имена полей в индексе
     */
    List<DomainDbIndexField> getFields();

    /**
     * Уникальный ли индекс
     */
    boolean isUnique();

    /**
     * Возвращает sql-представление списка полей для индекса.
     * Например: {@code name,name2 desc}
     */
    String getSqlFields();
}
