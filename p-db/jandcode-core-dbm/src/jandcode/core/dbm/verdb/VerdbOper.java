package jandcode.core.dbm.verdb;

import jandcode.core.dbm.mdb.*;

/**
 * Оператор changeset.
 * Один конкретный sql-оператор или groovy-скрипт.
 */
public interface VerdbOper extends IVerdbVersionLink {

    /**
     * Какому файлу принадлежит
     */
    VerdbFile getFile();

    /**
     * Выполнить оператор
     *
     * @param mdb для какой базы данных
     */
    void exec(Mdb mdb) throws Exception;

}
