package jandcode.core.dbm.sql;

/**
 * Построитель части where
 */
public interface SqlFilterWhereBuilder {

    /**
     * Построить часть where в указанном контексте
     *
     * @param ctx контекст построения where
     */
    void buildWhere(SqlFilterWhereContext ctx);

}
