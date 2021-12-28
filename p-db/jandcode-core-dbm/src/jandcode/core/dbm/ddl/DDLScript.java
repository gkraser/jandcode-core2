package jandcode.core.dbm.ddl;

import jandcode.commons.io.*;
import jandcode.commons.simxml.*;
import jandcode.core.dbm.*;
import jandcode.core.dbm.mdb.*;

import java.util.*;

/**
 * ddl скрипт.
 * Набор ddl операторов.
 */
public interface DDLScript extends IModelMember {

    /**
     * Список операций этого скрипта
     */
    List<DDLOper> getItems();

    /**
     * Выполнить этот скрипт для указанной mdb.
     */
    void exec(Mdb mdb) throws Exception;

    /**
     * Записать скрипт в xml
     */
    void saveToXml(SimXml x) throws Exception;

    /**
     * Загрузить скрипт из xml
     */
    void loadFromXml(SimXml x) throws Exception;

    /**
     * Загрузка из xml
     */
    LoadFrom load();

    /**
     * Запись в xml
     */
    SaveTo save();

}
