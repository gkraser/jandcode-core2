package jandcode.core.dbm.domain.db;

import jandcode.commons.named.*;

/**
 * Поле индекса
 */
public interface DomainDbIndexField extends INamed {

    /**
     * Сортировка в обратном порядке
     */
    boolean isDesc();

}
