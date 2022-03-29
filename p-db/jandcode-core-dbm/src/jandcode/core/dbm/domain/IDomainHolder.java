package jandcode.core.dbm.domain;

import jandcode.commons.named.*;

/**
 * Хранилище набора доменов
 */
public interface IDomainHolder {

    /**
     * Домены. Кешированный список.
     */
    NamedList<Domain> getDomains();

}
