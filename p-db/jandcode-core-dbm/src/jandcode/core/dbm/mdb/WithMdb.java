package jandcode.core.dbm.mdb;

/**
 * Выполнение кода в контексте Mdb
 */
public interface WithMdb {

    /**
     * Выполнить код в контексте mdb.
     */
    void withMdb(Mdb mdb) throws Exception;

}
