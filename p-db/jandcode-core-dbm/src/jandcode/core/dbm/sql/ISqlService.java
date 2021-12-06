package jandcode.core.dbm.sql;

/**
 * Методы сервиса sql
 */
public interface ISqlService {

    /**
     * Создать {@link SqlText} для указанного текста sql.
     */
    SqlText createSqlText(String sql);

}
