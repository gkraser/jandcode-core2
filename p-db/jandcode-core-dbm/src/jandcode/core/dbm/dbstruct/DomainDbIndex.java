package jandcode.core.dbm.dbstruct;

import jandcode.commons.named.*;

import java.util.*;

/**
 * Индекс для домена в базе данных
 */
public interface DomainDbIndex extends INamed {

    /**
     * Имена полей в индексе
     * conf: {@code <dbindex fields="f1,*f2,f3"/>}
     */
    List<DomainDbIndexField> getFields();

    /**
     * Уникальный ли индекс
     * conf: {@code <dbindex unique="true"/>}
     */
    boolean isUnique();

    /**
     * Возвращает sql-представление списка полей для индекса.
     * Например: {@code name,name2 desc}
     */
    String getSqlFields();

}
