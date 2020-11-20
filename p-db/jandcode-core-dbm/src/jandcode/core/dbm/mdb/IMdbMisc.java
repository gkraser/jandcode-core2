package jandcode.core.dbm.mdb;

import jandcode.core.store.*;

/**
 * Разные утилитные методы {@link Mdb}
 */
public interface IMdbMisc {

    ////// store

    /**
     * Создать пустой store со структурой как в домене.
     *
     * @param domainName для какого домена
     */
    Store createStore(String domainName);


}
