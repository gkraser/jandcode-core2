package jandcode.core.dbm.sql;

import jandcode.commons.conf.*;

/**
 * Методы сервиса sql
 */
public interface ISqlService {

    /**
     * Создать {@link SqlText} для указанного текста sql.
     */
    SqlText createSqlText(String sql);

    /**
     * Создать {@link SqlText} для указанной конфигурации.
     */
    SqlText createSqlText(Conf conf);

}
