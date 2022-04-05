package jandcode.core.dbm.verdb;

/**
 * Оператор changeset.
 * Один конкретный sql-оператор или groovy-скрипт.
 */
public interface VerdbOper extends IVerdbVersionLink {

    /**
     * Какому файлу принадлежит
     */
    VerdbFile getFile();

}
