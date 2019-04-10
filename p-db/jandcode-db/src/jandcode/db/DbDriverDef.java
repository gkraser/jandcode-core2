package jandcode.db;

import jandcode.core.*;
import jandcode.commons.conf.*;

/**
 * Объявление dbdriver
 */
public interface DbDriverDef extends Comp, IConfLink {

    /**
     * Создать экземпляр для указанного dbsource
     */
    DbDriver createInst(DbSource dbSource);

    /**
     * Тип базы данных для этого драйвера
     */
    String getDbType();

}
