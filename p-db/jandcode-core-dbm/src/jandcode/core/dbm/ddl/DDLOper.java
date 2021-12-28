package jandcode.core.dbm.ddl;

import jandcode.commons.simxml.*;
import jandcode.core.*;
import jandcode.core.dbm.mdb.*;

/**
 * Оператор ddl
 */
public interface DDLOper extends Comp {

    /**
     * Тип (sql, data ...)
     */
    String getType();

    /**
     * Выполнить этот оператор для указанной mdb.
     */
    void exec(Mdb mdb) throws Exception;

    /**
     * Сохранить этот DDL в указанном узле xml.
     */
    void saveToXml(SimXml x) throws Exception;

    /**
     * Загрузить этот DDL из указанного узла xml.
     * Формат такой же, который был использован
     * в методе {@link DDLOper#saveToXml(SimXml)}.
     */
    void loadFromXml(SimXml x) throws Exception;

    /**
     * Возвращает sql-скрипт.
     */
    String getSqlScript();

}
